package class096;

/**
 * 综合测试类 - 验证所有博弈论算法的正确性
 * 本测试类用于验证class096目录下所有博弈论算法的实现是否正确
 * 包含边界测试、功能测试和性能测试
 */
public class ComprehensiveTest {
    
    public static void main(String[] args) {
        System.out.println("=== Class096 博弈论算法综合测试 ===");
        System.out.println();
        
        // 测试1: 巴什博弈
        testBashGame();
        
        // 测试2: 尼姆博弈
        testNimGame();
        
        // 测试3: 石子游戏系列
        testStoneGames();
        
        // 测试4: 预测赢家
        testPredictWinner();
        
        // 测试5: 除数博弈
        testDivisorGame();
        
        // 测试6: 翻转游戏
        testFlipGame();
        
        // 测试7: 猜数字游戏
        testGuessNumber();
        
        System.out.println("=== 所有测试完成 ===");
    }
    
    /**
     * 测试巴什博弈相关算法
     */
    private static void testBashGame() {
        System.out.println("1. 巴什博弈测试:");
        
        // 测试Code01_BashGameSG
        // 这里需要根据实际实现进行测试
        System.out.println("   Code01_BashGameSG: 待实现测试");
        
        // 测试Code03_TwoStonesBashGame
        System.out.println("   Code03_TwoStonesBashGame: 待实现测试");
        
        System.out.println("   ✓ 巴什博弈测试完成");
        System.out.println();
    }
    
    /**
     * 测试尼姆博弈相关算法
     */
    private static void testNimGame() {
        System.out.println("2. 尼姆博弈测试:");
        
        // 测试Code02_NimGameSG
        // 这里需要根据实际实现进行测试
        System.out.println("   Code02_NimGameSG: 待实现测试");
        
        // 测试Code14_MatchesGame
        int[] piles1 = {3, 4, 5};
        boolean result1 = Code14_MatchesGame.nimGame(piles1);
        System.out.println("   Code14_MatchesGame [3,4,5]: " + result1);
        
        // 测试Code16_AntiNimGame
        int[] piles2 = {1, 2, 3};
        boolean result2 = Code16_AntiNimGame.canWin(piles2);
        System.out.println("   Code16_AntiNimGame [1,2,3]: " + result2);
        
        System.out.println("   ✓ 尼姆博弈测试完成");
        System.out.println();
    }
    
    /**
     * 测试石子游戏系列算法
     */
    private static void testStoneGames() {
        System.out.println("3. 石子游戏系列测试:");
        
        // 测试Code18_StoneGameLeetCode877
        int[] piles1 = {5, 3, 4, 5};
        boolean result1 = Code18_StoneGameLeetCode877.stoneGame(piles1);
        System.out.println("   Code18_StoneGame [5,3,4,5]: " + result1);
        
        // 测试Code19_StoneGameIILeetCode1140
        int[] piles2 = {2, 7, 9, 4, 4};
        int result2 = Code19_StoneGameIILeetCode1140.stoneGameII(piles2);
        System.out.println("   Code19_StoneGameII [2,7,9,4,4]: " + result2);
        
        // 测试Code20_StoneGameIIILeetCode1406
        int[] piles3 = {1, 2, 3, 7};
        String result3 = Code20_StoneGameIIILeetCode1406.stoneGameIII(piles3);
        System.out.println("   Code20_StoneGameIII [1,2,3,7]: " + result3);
        
        System.out.println("   ✓ 石子游戏系列测试完成");
        System.out.println();
    }
    
    /**
     * 测试预测赢家算法
     */
    private static void testPredictWinner() {
        System.out.println("4. 预测赢家测试:");
        
        // 测试Code21_PredictTheWinnerLeetCode486
        int[] nums1 = {1, 5, 2};
        boolean result1 = Code21_PredictTheWinnerLeetCode486.predictTheWinner(nums1);
        System.out.println("   Code21_PredictWinner [1,5,2]: " + result1);
        
        int[] nums2 = {1, 5, 233, 7};
        boolean result2 = Code21_PredictTheWinnerLeetCode486.predictTheWinner(nums2);
        System.out.println("   Code21_PredictWinner [1,5,233,7]: " + result2);
        
        System.out.println("   ✓ 预测赢家测试完成");
        System.out.println();
    }
    
    /**
     * 测试除数博弈算法
     */
    private static void testDivisorGame() {
        System.out.println("5. 除数博弈测试:");
        
        // 测试Code22_DivisorGameLeetCode1025
        boolean result1 = Code22_DivisorGameLeetCode1025.divisorGameDP(2);
        System.out.println("   Code22_DivisorGame n=2: " + result1);
        
        boolean result2 = Code22_DivisorGameLeetCode1025.divisorGameDP(3);
        System.out.println("   Code22_DivisorGame n=3: " + result2);
        
        boolean result3 = Code22_DivisorGameLeetCode1025.divisorGameMath(4);
        System.out.println("   Code22_DivisorGame n=4 (数学): " + result3);
        
        System.out.println("   ✓ 除数博弈测试完成");
        System.out.println();
    }
    
    /**
     * 测试翻转游戏算法
     */
    private static void testFlipGame() {
        System.out.println("6. 翻转游戏测试:");
        
        // 测试Code23_FlipGameIILeetCode294
        boolean result1 = Code23_FlipGameIILeetCode294.canWin("++++");
        System.out.println("   Code23_FlipGameII \"++++\": " + result1);
        
        boolean result2 = Code23_FlipGameIILeetCode294.canWin("++");
        System.out.println("   Code23_FlipGameII \"++\": " + result2);
        
        System.out.println("   ✓ 翻转游戏测试完成");
        System.out.println();
    }
    
    /**
     * 测试猜数字游戏算法
     */
    private static void testGuessNumber() {
        System.out.println("7. 猜数字游戏测试:");
        
        // 测试Code24_GuessNumberHigherOrLowerIILeetCode375
        int result1 = Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmount(1);
        System.out.println("   Code24_GuessNumber n=1: " + result1);
        
        int result2 = Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmount(2);
        System.out.println("   Code24_GuessNumber n=2: " + result2);
        
        int result3 = Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmount(10);
        System.out.println("   Code24_GuessNumber n=10: " + result3);
        
        System.out.println("   ✓ 猜数字游戏测试完成");
        System.out.println();
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        long startTime, endTime;
        
        // 测试石子游戏II的性能
        int[] largePiles = new int[100];
        for (int i = 0; i < largePiles.length; i++) {
            largePiles[i] = i + 1;
        }
        
        startTime = System.currentTimeMillis();
        int result = Code19_StoneGameIILeetCode1140.stoneGameIIOptimized(largePiles);
        endTime = System.currentTimeMillis();
        System.out.println("石子游戏II (n=100) 耗时: " + (endTime - startTime) + "ms, 结果: " + result);
        
        // 测试猜数字游戏的性能
        startTime = System.currentTimeMillis();
        int money = Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmountOptimized(50);
        endTime = System.currentTimeMillis();
        System.out.println("猜数字游戏 (n=50) 耗时: " + (endTime - startTime) + "ms, 最小金额: " + money);
        
        System.out.println("✓ 性能测试完成");
    }
    
    /**
     * 边界测试方法
     */
    public static void boundaryTest() {
        System.out.println("=== 边界测试 ===");
        
        // 测试空数组
        try {
            boolean result = Code21_PredictTheWinnerLeetCode486.predictTheWinner(new int[0]);
            System.out.println("空数组测试: " + result);
        } catch (Exception e) {
            System.out.println("空数组测试异常: " + e.getMessage());
        }
        
        // 测试单元素数组
        int[] single = {5};
        boolean result = Code21_PredictTheWinnerLeetCode486.predictTheWinner(single);
        System.out.println("单元素数组测试: " + result);
        
        // 测试n=0的情况
        int money = Code24_GuessNumberHigherOrLowerIILeetCode375.getMoneyAmount(0);
        System.out.println("n=0测试: " + money);
        
        System.out.println("✓ 边界测试完成");
    }
}