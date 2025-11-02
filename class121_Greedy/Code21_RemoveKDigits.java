package class090;

// 移除K个数字
// 题目描述：给定一个以字符串表示的非负整数 num，移除这个数中的 k 位数字，使得剩下的数字最小。
// 注意：输出不能含有前导零，但如果结果为0，必须保留这个零。
// 测试链接: https://leetcode.cn/problems/remove-k-digits/
public class Code21_RemoveKDigits {

    /**
     * 移除K个数字的贪心解法
     * 
     * 解题思路：
     * 1. 使用贪心策略，维护一个递增的序列
     * 2. 遍历字符串中的每一个字符，对于当前字符：
     *    a. 如果栈不为空，且栈顶元素大于当前字符，且还能移除数字（k>0），则弹出栈顶元素
     *    b. 将当前字符入栈
     * 3. 移除完k个数字后，如果还需要移除数字（可能序列是递增的），继续从栈顶移除
     * 4. 构建结果字符串，移除前导零
     * 5. 如果结果为空，返回"0"
     * 
     * 贪心策略的正确性：
     * - 为了使剩下的数字最小，我们希望前面的数字尽可能小
     * - 当遇到一个较小的数字时，如果前面有较大的数字且还能移除，应该移除前面较大的数字
     * - 这样可以保证前面的位置上的数字尽可能小，从而使整个数字最小
     * 
     * 时间复杂度：O(n)，其中n是字符串的长度
     * - 每个字符最多被入栈和出栈一次
     * 
     * 空间复杂度：O(n)，用于存储栈和结果字符串
     * 
     * @param num 以字符串表示的非负整数
     * @param k 需要移除的数字个数
     * @return 移除k个数字后得到的最小数字（字符串形式）
     */
    public static String removeKdigits(String num, int k) {
        // 边界条件处理
        if (num == null || num.isEmpty()) {
            return "0";
        }
        
        int n = num.length();
        // 如果需要移除的数字个数大于等于字符串长度，结果为"0"
        if (k >= n) {
            return "0";
        }

        // 使用字符数组模拟栈，提高效率
        char[] stack = new char[n];
        int stackSize = 0; // 当前栈的大小

        // 遍历字符串中的每一个字符
        for (int i = 0; i < n; i++) {
            char c = num.charAt(i);
            // 当栈不为空，且栈顶元素大于当前字符，且还能移除数字时，弹出栈顶元素
            while (stackSize > 0 && stack[stackSize - 1] > c && k > 0) {
                stackSize--;
                k--;
            }
            // 将当前字符入栈
            stack[stackSize++] = c;
        }

        // 如果还需要移除数字，从栈顶继续移除
        while (k > 0) {
            stackSize--;
            k--;
        }

        // 构建结果字符串，移除前导零
        StringBuilder result = new StringBuilder();
        boolean leadingZero = true;
        for (int i = 0; i < stackSize; i++) {
            if (leadingZero && stack[i] == '0') {
                continue; // 跳过前导零
            }
            leadingZero = false;
            result.append(stack[i]);
        }

        // 如果结果为空，返回"0"
        return result.length() == 0 ? "0" : result.toString();
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        // 输入: num = "1432219", k = 3
        // 输出: "1219"
        // 解释: 移除掉三个数字 4, 3, 和 2 后，得到 1219
        System.out.println("测试用例1结果: " + removeKdigits("1432219", 3)); // 期望输出: "1219"

        // 测试用例2
        // 输入: num = "10200", k = 1
        // 输出: "200"
        // 解释: 移除掉第一个 1 得到 200，注意前导零被忽略
        System.out.println("测试用例2结果: " + removeKdigits("10200", 1)); // 期望输出: "200"

        // 测试用例3
        // 输入: num = "10", k = 2
        // 输出: "0"
        // 解释: 需要移除两个数字，只剩下空字符串，返回"0"
        System.out.println("测试用例3结果: " + removeKdigits("10", 2)); // 期望输出: "0"

        // 测试用例4：边界情况 - 递增序列
        // 输入: num = "12345", k = 2
        // 输出: "123"
        System.out.println("测试用例4结果: " + removeKdigits("12345", 2)); // 期望输出: "123"

        // 测试用例5：边界情况 - 递减序列
        // 输入: num = "54321", k = 2
        // 输出: "321"
        System.out.println("测试用例5结果: " + removeKdigits("54321", 2)); // 期望输出: "321"

        // 测试用例6：包含前导零的情况
        // 输入: num = "10001", k = 1
        // 输出: "0001" -> "1"
        System.out.println("测试用例6结果: " + removeKdigits("10001", 1)); // 期望输出: "1"

        // 测试用例7：更复杂的情况
        // 输入: num = "10200", k = 1
        // 输出: "200"
        System.out.println("测试用例7结果: " + removeKdigits("10200", 1)); // 期望输出: "200"
    }
}