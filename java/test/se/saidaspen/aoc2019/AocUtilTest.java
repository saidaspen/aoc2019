package se.saidaspen.aoc2019;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class AocUtilTest {

    @Test
    public void readsInputFiles() throws Exception {
        assertFalse(AocUtil.input(1).isEmpty());
        assertFalse(AocUtil.input(5).isEmpty());
        assertFalse(AocUtil.input(10).isEmpty());
    }

    @Test (expected = AssertionError.class)
    public void throwsExceptionOnWrongDayNum() throws Exception {
        assertFalse(AocUtil.input(0).isEmpty());
    }

    @Test (expected = AssertionError.class)
    public void throwsExceptionOnWrongDayNum2() throws Exception {
        assertFalse(AocUtil.input(26).isEmpty());
    }

    @Test (expected = AssertionError.class)
    public void throwsExceptionFileNotExist() throws Exception {
        assertFalse(AocUtil.input(25).isEmpty());
    }

}
