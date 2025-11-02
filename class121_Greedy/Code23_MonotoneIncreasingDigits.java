package class090;

// 单调递增的数字
// 当且仅当每个相邻位数上的数字 x 和 y 满足 x <= y 时，我们称这个整数是单调递增的
// 给定一个整数 n ，返回 小于或等于 n 的最大数字，且数字呈单调递增
// 测试链接: https://leetcode.cn/problems/monotone-increasing-digits/
public class Code23_MonotoneIncreasingDigits {

    /**
     * 单调递增的数字问题的贪心解法
     * 
     * 解题思路：
     * 1. 从右向左遍历数字，找到第一个不满足单调递增的位置
     * 2. 将该位置减1，并将后面的所有数字都设为9
     * 3. 重复这个过程直到整个数字满足单调递增
     * 
     * 贪心策略的正确性：
     * 局部最优：遇到strNum[i - 1] > strNum[i]的情况，让strNum[i - 1]减一，strNum[i]设为9
     * 全局最优：得到小于等于N的最大单调递增整数
     * 
     * 时间复杂度：O(d)，其中d是数字的位数
     * 空间复杂度：O(d)，需要将数字转换为字符数组
     * 
     * @param n 输入数字
     * @return 小于或等于n的最大单调递增数字
     */
    public static int monotoneIncreasingDigits(int n) {
        // 边界条件处理
        if (n < 10) return n;
        
        // 将数字转换为字符数组便于处理
        char[] digits = String.valueOf(n).toCharArray();
        int len = digits.length;
        
        // 标记需要修改的位置
        int flag = len;
        
        // 从右向左遍历，找到第一个不满足单调递增的位置
        for (int i = len - 1; i > 0; i--) {
            if (digits[i] < digits[i - 1]) {
                // 当前位置需要减1
                digits[i - 1]--;
                // 标记从当前位置开始需要设为9
                flag = i;
            }
        }
        
        // 将标记位置及之后的所有数字设为9
        for (int i = flag; i < len; i++) {
            digits[i] = '9';
        }
        
        // 将字符数组转换回数字
        return Integer.parseInt(new String(digits));
    }

    /**
     * 单调递增的数字问题的另一种解法（更直观）
     * 
     * 解题思路：
     * 1. 从左向右遍历，找到第一个不满足单调递增的位置
     * 2. 从该位置开始向前回溯，找到需要减1的位置
     * 3. 将该位置减1，后面的所有位置设为9
     * 
     * 时间复杂度：O(d)
     * 空间复杂度：O(d)
     */
    public static int monotoneIncreasingDigits2(int n) {
        if (n < 10) return n;
        
        char[] digits = String.valueOf(n).toCharArray();
        int len = digits.length;
        
        // 从左向右找到第一个不满足单调递增的位置
        int i = 1;
        while (i < len && digits[i] >= digits[i - 1]) {
            i++;
        }
        
        // 如果整个数字已经单调递增，直接返回
        if (i == len) return n;
        
        // 向前回溯，找到需要减1的位置
        while (i > 0 && digits[i] < digits[i - 1]) {
            digits[i - 1]--;
            i--;
        }
        
        // 将后面的所有数字设为9
        for (i = i + 1; i < len; i++) {
            digits[i] = '9';
        }
        
        return Integer.parseInt(new String(digits));
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        // 输入: n = 10
        // 输出: 9
        // 解释: 10不是单调递增数字，最大单调递增数字是9
        System.out.println("测试用例1结果: " + monotoneIncreasingDigits(10)); // 期望输出: 9
        
        // 测试用例2
        // 输入: n = 1234
        // 输出: 1234
        // 解释: 1234本身就是单调递增数字
        System.out.println("测试用例2结果: " + monotoneIncreasingDigits(1234)); // 期望输出: 1234
        
        // 测试用例3
        // 输入: n = 332
        // 输出: 299
        // 解释: 332不是单调递增，最大单调递增数字是299
        System.out.println("测试用例3结果: " + monotoneIncreasingDigits(332)); // 期望输出: 299
        
        // 测试用例4：边界情况
        // 输入: n = 1
        // 输出: 1
        System.out.println("测试用例4结果: " + monotoneIncreasingDigits(1)); // 期望输出: 1
        
        // 测试用例5：复杂情况
        // 输入: n = 100
        // 输出: 99
        System.out.println("测试用例5结果: " + monotoneIncreasingDigits(100)); // 期望输出: 99
        
        // 测试用例6
        // 输入: n = 1234321
        // 输出: 1233999
        System.out.println("测试用例6结果: " + monotoneIncreasingDigits(1234321)); // 期望输出: 1233999
    }
}