package agh.ics.oop.model;

import agh.ics.oop.OptionsParser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class OptionsParserTest {
    @Test
    public void ParseWithOnlyValidLettersInInput() {
        String[] input = {"f", "f", "b", "f", "l", "l", "r", "l"};

        List<MoveDirection> output = OptionsParser.parse(input);

        MoveDirection[] expectedTab = {MoveDirection.FORWARD, MoveDirection.FORWARD, MoveDirection.BACKWARD, MoveDirection.FORWARD, MoveDirection.LEFT, MoveDirection.LEFT, MoveDirection.RIGHT, MoveDirection.LEFT};
        List<MoveDirection> expected = new ArrayList<>(Arrays.asList(expectedTab));

        assertEquals(expected, output);
    }
    @Test
    public void ParseWithInvalidLettersInInput() {
        String[] input = {"f", "dada","grdeg","a", "f","2", "b", "l","12131", "r","rtee45", "l"};

        try {
            OptionsParser.parse(input);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            assertEquals("dada is not legal move specification", message);
        }

    }
    @Test
    public void ParseWithOnlyInvalidInput() {
        String[] input = {"g", "a", "2", "434", "adda", "lflflfllllf", "asd", "wdi"};

        try {
            OptionsParser.parse(input);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            assertEquals("g is not legal move specification", message);
        }
    }
    @Test
    public void ParseWithEmptyInput() {
        String[] input = {};
        List<MoveDirection> output = OptionsParser.parse(input);
        MoveDirection[] expectedTab = new MoveDirection[] {};
        List<MoveDirection> expected = new ArrayList<>(Arrays.asList(expectedTab));
        assertEquals(expected, output);
    }
    @Test
    public void ParseUpperCaseInput() {
        String[] input = {"L", "R", "L", "R", "F", "B"};
        try {
            OptionsParser.parse(input);
            fail("IllegalArgumentException was expected but not thrown");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            assertEquals("L is not legal move specification", message);
        }

    }
}
