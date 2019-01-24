import org.junit.jupiter.api.*;

class CalculatorTest {

    @Test
    void firstNegative() {
        Assertions.assertEquals(Calculator.postfixExp("-2+5"), "3", "-2 + 5 = 3");
        Assertions.assertEquals(Calculator.postfixExp("---5+7"), "2", "-5 + 7 = 2");
        Assertions.assertEquals(Calculator.postfixExp("-5+(2)"), "-3", "-5 + 2 = -3");
    }

    @Test
    void parenthesesMultiply() {
        Assertions.assertEquals(Calculator.postfixExp("(5)(4)"), "20", "5 * 4 = 20");
        Assertions.assertEquals(Calculator.postfixExp("(-5)(4)"), "-20", "-5 * 4 = -20");
        Assertions.assertEquals(Calculator.postfixExp("5(-4)"), "-20", "5 * -4 = -20");
        Assertions.assertEquals(Calculator.postfixExp("-5(-4)"), "20", "-5 * -4 = 20");
    }

    @Test
    void absoluteExp() {
        Assertions.assertEquals(Calculator.postfixExp("[-6]"), "6", "|-6| = 6");
        Assertions.assertEquals(Calculator.postfixExp("[-6[-7[-8]]]"), "336", "|-6|-7|-8||| = 336");
        Assertions.assertEquals(Calculator.postfixExp("[-6[-7(-8)]]"), "336", "|-6|-7(-8)|| = 336");
    }

    @Test
    void longExp() {
        Assertions.assertEquals(Calculator.postfixExp("5 + 2([4 - 77] - 8)+2^3"), "143", "5 + 2(|4 - 77| - 8)+2^3 = 143");
        Assertions.assertEquals(Calculator.postfixExp("3[[4-6]-22]2^3"), "480", "3||4-6|-22|2^3");
        Assertions.assertEquals(Calculator.postfixExp("4* (4+2)(3+3) - 5"), "139", "4* (4+2)(3+3) - 5 = 139");
        Assertions.assertEquals(Calculator.postfixExp("9(3)(2)[3-6]5(2)"), "1620", "9(3)(2)|3-6|5(2) = 1620");
    }

    @Test
    void multiplePlusSign() {
        Assertions.assertEquals(Calculator.postfixExp("+++++5"), "5", "5");
        Assertions.assertEquals(Calculator.postfixExp("+++++5+++3"), "8", "5+3=8");
    }

    @Test
    void multipleMinusSign() {
        Assertions.assertEquals(Calculator.postfixExp("-----5"), "-5", "-5");
        Assertions.assertEquals(Calculator.postfixExp("-----5---3"), "-8", "-5-3=-8");
    }

    @Test
    void plusMinusSign() {
        Assertions.assertEquals(Calculator.postfixExp("5+-3"), "2", "5 - 3 = 2");
        Assertions.assertEquals(Calculator.postfixExp("5-+3"), "2", "5 - 3 = 2");
    }

    @Test
    void mdpMinusSign() {
        Assertions.assertEquals(Calculator.postfixExp("6*-3"), "-18", "6 * -3 = -18");
        Assertions.assertEquals(Calculator.postfixExp("6/-3"), "-2", "6 / -3 = -2");
        Assertions.assertEquals(Calculator.postfixExp("5%-3"), "-1", "5 % -3 = 1");
    }
}
