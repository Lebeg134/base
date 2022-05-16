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
        userDummy = new TrainUser() {
            boolean alarm = false;
            @Override
            public int getJoystickPosition() {
                return 0;
            }

            @Override
            public boolean getAlarmFlag() {
                return false;
            }

            @Override
            public void overrideJoystickPosition(int joystickPosition) {

            }

            @Override
            public boolean isAlarmState() {
                return alarm;
            }

            @Override
            public void setAlarmState(boolean alarmState) {
                alarm = alarmState;
            }
        };
        sensor = new TrainSensorImpl(controllerDummy, userDummy);
        Assert.assertFalse(userDummy.isAlarmState());

        mock = mock(TrainController.class);
    }


    @Test
    public void Nominal() { //Normal working conditions, no alarm should be fired. checks limit modification
        sensor.overrideSpeedLimit(250);
        Assert.assertFalse(userDummy.isAlarmState());
        Assert.assertEquals(250, sensor.getSpeedLimit());
        sensor.overrideSpeedLimit(200);
        Assert.assertFalse(userDummy.isAlarmState());
        Assert.assertEquals(200, sensor.getSpeedLimit());
    }
    @Test
    public void TopAlarm(){
        sensor.overrideSpeedLimit(499);
        Assert.assertFalse(userDummy.isAlarmState());
        Assert.assertEquals(499, sensor.getSpeedLimit());
        sensor.overrideSpeedLimit(501);
        Assert.assertTrue(userDummy.isAlarmState());
        Assert.assertEquals(501, sensor.getSpeedLimit());
    }
    @Test
    public void BottomAlarm(){
        sensor.overrideSpeedLimit(0);
        Assert.assertFalse(userDummy.isAlarmState());
        Assert.assertEquals(0, sensor.getSpeedLimit());
        sensor.overrideSpeedLimit(-1);
        Assert.assertTrue(userDummy.isAlarmState());
        Assert.assertEquals(-1, sensor.getSpeedLimit());
    }
    @Test
    public void DifferenceAlarm(){
        sensor.overrideSpeedLimit(499);
        Assert.assertFalse(userDummy.isAlarmState());
        Assert.assertEquals(499, sensor.getSpeedLimit());
        sensor.overrideSpeedLimit(1);
        Assert.assertTrue(userDummy.isAlarmState());
        Assert.assertEquals(1, sensor.getSpeedLimit());

    }
}
