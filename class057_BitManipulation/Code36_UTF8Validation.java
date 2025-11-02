package class031;

/**
 * UTF-8 编码验证
 * 测试链接：https://leetcode.cn/problems/utf-8-validation/
 * 
 * 题目描述：
 * 给定一个表示数据的整数数组 data，返回它是否为有效的 UTF-8 编码。
 * UTF-8 中的一个字符可能的长度为 1 到 4 字节，遵循以下规则：
 * 1. 对于 1 字节的字符，字节的第一位设为 0，后面 7 位为这个符号的 Unicode 码。
 * 2. 对于 n 字节的字符 (n > 1)，第一个字节的前 n 位都设为 1，第 n+1 位设为 0，
 *    后面字节的前两位一律设为 10。
 * 
 * 示例：
 * 输入：data = [197,130,1]
 * 输出：true
 * 解释：数据表示 2 字节的字符后跟 1 字节的字符。
 * 
 * 输入：data = [235,140,4]
 * 输出：false
 * 解释：第一个字节表示这是一个 3 字节字符，但第二个字节不以 10 开头。
 * 
 * 提示：
 * 1 <= data.length <= 2 * 10^4
 * 0 <= data[i] <= 255
 * 
 * 解题思路：
 * 1. 逐字节验证：按UTF-8编码规则逐个字节验证
 * 2. 状态机：使用状态机跟踪当前字符的字节数
 * 3. 位运算：使用位掩码检查字节格式
 * 
 * 时间复杂度分析：
 * - 所有方法：O(n)，n为数组长度
 * 
 * 空间复杂度分析：
 * - 所有方法：O(1)，只使用常数空间
 */
public class Code36_UTF8Validation {
    
    /**
     * 方法1：逐字节验证（推荐）
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public boolean validUtf8_1(int[] data) {
        int n = data.length;
        int i = 0;
        
        while (i < n) {
            // 获取当前字节
            int current = data[i];
            
            // 判断当前字节的类型
            int type = getByteType(current);
            
            // 检查类型是否有效
            if (type == -1) {
                return false;
            }
            
            // 检查后续字节数量是否足够
            if (i + type > n) {
                return false;
            }
            
            // 验证后续字节（如果是多字节字符）
            for (int j = 1; j < type; j++) {
                if (!isContinuationByte(data[i + j])) {
                    return false;
                }
            }
            
            i += type;
        }
        
        return true;
    }
    
    /**
     * 方法2：状态机实现
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public boolean validUtf8_2(int[] data) {
        int expectedBytes = 0; // 期望的后续字节数
        
        for (int current : data) {
            if (expectedBytes == 0) {
                // 新的字符开始
                if ((current & 0x80) == 0) {
                    // 1字节字符：0xxxxxxx
                    expectedBytes = 0;
                } else if ((current & 0xE0) == 0xC0) {
                    // 2字节字符：110xxxxx
                    expectedBytes = 1;
                } else if ((current & 0xF0) == 0xE0) {
                    // 3字节字符：1110xxxx
                    expectedBytes = 2;
                } else if ((current & 0xF8) == 0xF0) {
                    // 4字节字符：11110xxx
                    expectedBytes = 3;
                } else {
                    return false; // 无效的首字节
                }
            } else {
                // 检查后续字节格式：10xxxxxx
                if ((current & 0xC0) != 0x80) {
                    return false;
                }
                expectedBytes--;
            }
        }
        
        return expectedBytes == 0; // 所有字符必须完整
    }
    
    /**
     * 方法3：位掩码优化版
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public boolean validUtf8_3(int[] data) {
        int count = 0; // 剩余需要验证的后续字节数
        
        for (int num : data) {
            if (count == 0) {
                if ((num >> 5) == 0b110) {
                    count = 1;
                } else if ((num >> 4) == 0b1110) {
                    count = 2;
                } else if ((num >> 3) == 0b11110) {
                    count = 3;
                } else if ((num >> 7) != 0) {
                    return false; // 无效的首字节
                }
            } else {
                if ((num >> 6) != 0b10) {
                    return false;
                }
                count--;
            }
        }
        
        return count == 0;
    }
    
    /**
     * 方法4：详细的位运算验证
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public boolean validUtf8_4(int[] data) {
        int index = 0;
        int n = data.length;
        
        while (index < n) {
            int firstByte = data[index];
            
            // 检查1字节字符
            if ((firstByte & 0x80) == 0) {
                index++;
                continue;
            }
            
            // 检查多字节字符
            int byteCount = getByteCount(firstByte);
            if (byteCount == -1 || index + byteCount > n) {
                return false;
            }
            
            // 验证后续字节
            for (int i = 1; i < byteCount; i++) {
                if (!isValidContinuation(data[index + i])) {
                    return false;
                }
            }
            
            index += byteCount;
        }
        
        return true;
    }
    
    // ========== 辅助方法 ==========
    
    /**
     * 获取字节类型（字符的字节数）
     * @param b 字节值
     * @return 字符字节数，-1表示无效
     */
    private int getByteType(int b) {
        if ((b & 0x80) == 0) return 1;        // 0xxxxxxx
        if ((b & 0xE0) == 0xC0) return 2;     // 110xxxxx
        if ((b & 0xF0) == 0xE0) return 3;     // 1110xxxx
        if ((b & 0xF8) == 0xF0) return 4;     // 11110xxx
        return -1; // 无效字节
    }
    
    /**
     * 检查是否为有效的后续字节
     * @param b 字节值
     * @return 是否有效
     */
    private boolean isContinuationByte(int b) {
        return (b & 0xC0) == 0x80; // 10xxxxxx
    }
    
    /**
     * 获取字符字节数
     * @param firstByte 首字节
     * @return 字节数，-1表示无效
     */
    private int getByteCount(int firstByte) {
        if ((firstByte & 0x80) == 0) return 1;
        if ((firstByte & 0xE0) == 0xC0) return 2;
        if ((firstByte & 0xF0) == 0xE0) return 3;
        if ((firstByte & 0xF8) == 0xF0) return 4;
        return -1;
    }
    
    /**
     * 检查是否为有效的后续字节
     * @param b 字节值
     * @return 是否有效
     */
    private boolean isValidContinuation(int b) {
        return (b & 0xC0) == 0x80;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code36_UTF8Validation solution = new Code36_UTF8Validation();
        
        // 测试用例1：有效UTF-8编码
        int[] data1 = {197, 130, 1}; // 2字节字符 + 1字节字符
        boolean result1 = solution.validUtf8_1(data1);
        System.out.println("测试用例1 - 输入: [197, 130, 1]");
        System.out.println("结果: " + result1 + " (预期: true)");
        
        // 测试用例2：无效UTF-8编码
        int[] data2 = {235, 140, 4}; // 3字节字符但第二个字节无效
        boolean result2 = solution.validUtf8_1(data2);
        System.out.println("测试用例2 - 输入: [235, 140, 4]");
        System.out.println("结果: " + result2 + " (预期: false)");
        
        // 测试用例3：单字节字符
        int[] data3 = {65, 66, 67}; // ASCII字符
        boolean result3 = solution.validUtf8_1(data3);
        System.out.println("测试用例3 - 输入: [65, 66, 67]");
        System.out.println("结果: " + result3 + " (预期: true)");
        
        // 测试用例4：混合字符
        int[] data4 = {227, 129, 130, 65}; // 3字节字符 + ASCII字符
        boolean result4 = solution.validUtf8_1(data4);
        System.out.println("测试用例4 - 输入: [227, 129, 130, 65]");
        System.out.println("结果: " + result4 + " (预期: true)");
        
        // 测试用例5：不完整的字符
        int[] data5 = {240, 162, 130}; // 4字节字符但缺少最后一个字节
        boolean result5 = solution.validUtf8_1(data5);
        System.out.println("测试用例5 - 输入: [240, 162, 130]");
        System.out.println("结果: " + result5 + " (预期: false)");
        
        // 性能测试
        int[] largeData = new int[10000];
        for (int i = 0; i < largeData.length; i++) {
            largeData[i] = 65; // 全部是ASCII字符
        }
        
        long startTime = System.currentTimeMillis();
        boolean perfResult = solution.validUtf8_1(largeData);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 - 输入长度: " + largeData.length);
        System.out.println("结果: " + perfResult);
        System.out.println("耗时: " + (endTime - startTime) + "ms");
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("所有方法:");
        System.out.println("  时间复杂度: O(n) - 遍历数组一次");
        System.out.println("  空间复杂度: O(1) - 只使用常数空间");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 边界处理：检查数组长度和字节范围");
        System.out.println("2. 性能优化：使用位运算提高效率");
        System.out.println("3. 可读性：清晰的变量命名和注释");
        System.out.println("4. 错误处理：详细的错误信息（实际工程中）");
        
        // 算法技巧总结
        System.out.println("\n=== 算法技巧总结 ===");
        System.out.println("1. 位掩码：使用位掩码检查字节格式");
        System.out.println("2. 状态机：跟踪当前字符的字节数");
        System.out.println("3. 提前终止：发现无效字节立即返回");
        System.out.println("4. 边界检查：确保不越界访问数组");
        
        // UTF-8编码规则总结
        System.out.println("\n=== UTF-8编码规则 ===");
        System.out.println("1字节: 0xxxxxxx");
        System.out.println("2字节: 110xxxxx 10xxxxxx");
        System.out.println("3字节: 1110xxxx 10xxxxxx 10xxxxxx");
        System.out.println("4字节: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx");
    }
}