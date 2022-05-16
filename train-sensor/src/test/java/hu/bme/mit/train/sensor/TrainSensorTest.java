package hu.bme.mit.train.sensor;

import hu.bme.mit.train.interfaces.TrainController;
import hu.bme.mit.train.interfaces.TrainSensor;
import hu.bme.mit.train.interfaces.TrainUser;
import hu.bme.mit.train.user.TrainUserImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class TrainSensorTest {
    TrainSensor sensor;
    TrainController controllerDummy;
    TrainUser userDummy;

    TrainController mock;
    @Before
    public void before() {
        controllerDummy = new TrainController() {
            int speed;
            @Override
            public void followSpeed() {

            }

            @Override
            public int getReferenceSpeed() {
                return speed;
            }

            @Override
            public void setSpeedLimit(int speedLimit) {
                speed = speedLimit;
            }

            @Override
            public void setJoystickPosition(int joystickPosition) {

            }
        };
        userDummy = mock(TrainUser.class);
        sensor = new TrainSensorImpl(controllerDummy, userDummy);
    }


    @Test
    public void Nominal() { //Normal working conditions, no alarm should be fired. checks limit modification
        sensor.overrideSpeedLimit(250);
        verify(userDummy, times(0)).setAlarmState(true);
        Assert.assertEquals(250, sensor.getSpeedLimit());
        sensor.overrideSpeedLimit(200);
        verify(userDummy, times(0)).setAlarmState(true);
        Assert.assertEquals(200, sensor.getSpeedLimit());
    }
    @Test
    public void TopAlarm(){
        sensor.overrideSpeedLimit(499);
        verify(userDummy, times(0)).setAlarmState(true);
        Assert.assertEquals(499, sensor.getSpeedLimit());
        sensor.overrideSpeedLimit(501);
        verify(userDummy, times(1)).setAlarmState(true);
        Assert.assertEquals(501, sensor.getSpeedLimit());
    }
    @Test
    public void BottomAlarm(){
        sensor.overrideSpeedLimit(0);
        verify(userDummy, times(0)).setAlarmState(true);
        Assert.assertEquals(0, sensor.getSpeedLimit());
        sensor.overrideSpeedLimit(-1);
        verify(userDummy, times(1)).setAlarmState(true);
        Assert.assertEquals(-1, sensor.getSpeedLimit());
    }
    @Test
    public void DifferenceAlarm(){
        sensor.overrideSpeedLimit(499);
        verify(userDummy, times(0)).setAlarmState(true);
        Assert.assertEquals(499, sensor.getSpeedLimit());
        sensor.overrideSpeedLimit(1);
        verify(userDummy, times(1)).setAlarmState(true);
        Assert.assertEquals(1, sensor.getSpeedLimit());

    }
}
