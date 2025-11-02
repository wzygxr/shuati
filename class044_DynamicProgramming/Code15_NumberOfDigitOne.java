// 数字1的个数 (Number of Digit One)
// 给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数。
// 测试链接 : https://leetcode.cn/problems/number-of-digit-one/
public class Code15_NumberOfDigitOne {

    // 方法1：暴力解法
    // 时间复杂度：O(n*log10(n)) - 需要遍历每个数字，并计算每个数字中1的个数
    // 空间复杂度：O(1) - 只使用常数额外空间
    // 问题：当n很大时效率低下
    public static int countDigitOne1(int n) {
        int count = 0;
        for (int i = 1; i <= n; i++) {
            count += countOnesInNumber(i);
        }
        return count;
    }

    // 计算一个数字中1的个数
    private static int countOnesInNumber(int num) {
        int count = 0;
        while (num > 0) {
            if (num % 10 == 1) {
                count++;
            }
            num /= 10;
        }
        return count;
    }

    // 方法2：数位DP解法
    // 时间复杂度：O(log10(n)) - 按数位处理
    // 空间复杂度：O(log10(n)) - 递归调用栈和dp数组
    public static int countDigitOne2(int n) {
        if (n <= 0) return 0;
        // 将数字转换为字符数组，方便按位处理
        char[] digits = String.valueOf(n).toCharArray();
        int len = digits.length;
        // dp[pos][count][limit] 表示处理到第pos位，已经出现了count个1，是否受限的方案数
        int[][][] dp = new int[len][len + 1][2];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j <= len; j++) {
                for (int k = 0; k < 2; k++) {
                    dp[i][j][k] = -1;
                }
            }
        }
        return f(digits, 0, 0, true, dp);
    }

    // 数位DP递归函数
    // digits: 数字的字符数组表示
    // pos: 当前处理的位置
    // count: 已经出现的1的个数
    // limit: 是否受到原数字的限制
    // dp: 记忆化数组
    private static int f(char[] digits, int pos, int count, boolean limit, int[][][] dp) {
        // base case
        if (pos == digits.length) {
            return count;
        }
        if (!limit && dp[pos][count][limit ? 1 : 0] != -1) {
            return dp[pos][count][limit ? 1 : 0];
        }
        
        int ans = 0;
        // 确定当前位可以填的数字范围
        int maxDigit = limit ? digits[pos] - '0' : 9;
        for (int digit = 0; digit <= maxDigit; digit++) {
            // 递归处理下一位
            ans += f(digits, pos + 1, count + (digit == 1 ? 1 : 0), limit && digit == maxDigit, dp);
        }
        
        if (!limit) {
            dp[pos][count][limit ? 1 : 0] = ans;
        }
        return ans;
    }

    // 方法3：数学规律解法
    // 时间复杂度：O(log10(n)) - 按数位处理
    // 空间复杂度：O(1) - 只使用常数额外空间
    public static int countDigitOne3(int n) {
        if (n <= 0) return 0;
        int count = 0;
        long factor = 1; // 当前位的权重（个位、十位、百位等）
        
        while (factor <= n) {
            // 计算当前位上1的个数
            // high: 当前位之前的高位数字
            // cur: 当前位的数字
            // low: 当前位之后的低位数字
            long high = n / (factor * 10);
            long cur = (n / factor) % 10;
            long low = n % factor;
            
            if (cur == 0) {
                // 当前位为0，1的个数由高位决定
                count += high * factor;
            } else if (cur == 1) {
                // 当前位为1，1的个数由高位和低位共同决定
                count += high * factor + low + 1;
            } else {
                // 当前位大于1，1的个数由高位决定
                count += (high + 1) * factor;
            }
            
            factor *= 10;
        }
        return count;
    }

    // 测试用例和性能对比
    public static void main(String[] args) {
        System.out.println("测试数字1的个数实现：");
        
        // 测试用例1
        int n1 = 13;
        System.out.println("n = " + n1);
        // 由于方法1效率较低，只在小数据上测试
        if (n1 <= 1000) {
            System.out.println("方法1 (暴力解法): " + countDigitOne1(n1));
        }
        System.out.println("方法2 (数位DP): " + countDigitOne2(n1));
        System.out.println("方法3 (数学规律): " + countDigitOne3(n1));
        
        // 测试用例2
        int n2 = 0;
        System.out.println("\nn = " + n2);
        System.out.println("方法2 (数位DP): " + countDigitOne2(n2));
        System.out.println("方法3 (数学规律): " + countDigitOne3(n2));
        
        // 测试用例3
        int n3 = 100;
        System.out.println("\nn = " + n3);
        if (n3 <= 1000) {
            System.out.println("方法1 (暴力解法): " + countDigitOne1(n3));
        }
        System.out.println("方法2 (数位DP): " + countDigitOne2(n3));
        System.out.println("方法3 (数学规律): " + countDigitOne3(n3));
    }
}