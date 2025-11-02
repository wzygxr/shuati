#include <iostream>
#include <vector>
#include <bitset>
#include <chrono>

using namespace std;

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
class Solution {
public:
    /**
     * 方法1：逐字节验证（推荐）
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    bool validUtf8_1(vector<int>& data) {
        int n = data.size();
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
    bool validUtf8_2(vector<int>& data) {
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
    bool validUtf8_3(vector<int>& data) {
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
    bool validUtf8_4(vector<int>& data) {
        int index = 0;
        int n = data.size();
        
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

private:
    /**
     * 获取字节类型（字符的字节数）
     * @param b 字节值
     * @return 字符字节数，-1表示无效
     */
    int getByteType(int b) {
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
    bool isContinuationByte(int b) {
        return (b & 0xC0) == 0x80; // 10xxxxxx
    }
    
    /**
     * 获取字符字节数
     * @param firstByte 首字节
     * @return 字节数，-1表示无效
     */
    int getByteCount(int firstByte) {
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
    bool isValidContinuation(int b) {
        return (b & 0xC0) == 0x80;
    }
};

/**
 * 测试函数
 */
int main() {
    Solution solution;
    
    // 测试用例1：有效UTF-8编码
    vector<int> data1 = {197, 130, 1}; // 2字节字符 + 1字节字符
    bool result1 = solution.validUtf8_1(data1);
    cout << "测试用例1 - 输入: [197, 130, 1]" << endl;
    cout << "结果: " << (result1 ? "true" : "false") << " (预期: true)" << endl;
    
    // 测试用例2：无效UTF-8编码
    vector<int> data2 = {235, 140, 4}; // 3字节字符但第二个字节无效
    bool result2 = solution.validUtf8_1(data2);
    cout << "测试用例2 - 输入: [235, 140, 4]" << endl;
    cout << "结果: " << (result2 ? "true" : "false") << " (预期: false)" << endl;
    
    // 测试用例3：单字节字符
    vector<int> data3 = {65, 66, 67}; // ASCII字符
    bool result3 = solution.validUtf8_1(data3);
    cout << "测试用例3 - 输入: [65, 66, 67]" << endl;
    cout << "结果: " << (result3 ? "true" : "false") << " (预期: true)" << endl;
    
    // 测试用例4：混合字符
    vector<int> data4 = {227, 129, 130, 65}; // 3字节字符 + ASCII字符
    bool result4 = solution.validUtf8_1(data4);
    cout << "测试用例4 - 输入: [227, 129, 130, 65]" << endl;
    cout << "结果: " << (result4 ? "true" : "false") << " (预期: true)" << endl;
    
    // 测试用例5：不完整的字符
    vector<int> data5 = {240, 162, 130}; // 4字节字符但缺少最后一个字节
    bool result5 = solution.validUtf8_1(data5);
    cout << "测试用例5 - 输入: [240, 162, 130]" << endl;
    cout << "结果: " << (result5 ? "true" : "false") << " (预期: false)" << endl;
    
    // 性能测试
    vector<int> largeData(10000, 65); // 全部是ASCII字符
    
    auto start = chrono::high_resolution_clock::now();
    bool perfResult = solution.validUtf8_1(largeData);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "性能测试 - 输入长度: " << largeData.size() << endl;
    cout << "结果: " << (perfResult ? "true" : "false") << endl;
    cout << "耗时: " << duration.count() << "微秒" << endl;
    
    // 复杂度分析
    cout << "\n=== 复杂度分析 ===" << endl;
    cout << "所有方法:" << endl;
    cout << "  时间复杂度: O(n) - 遍历数组一次" << endl;
    cout << "  空间复杂度: O(1) - 只使用常数空间" << endl;
    
    // 工程化考量
    cout << "\n=== 工程化考量 ===" << endl;
    cout << "1. 边界处理：检查数组长度和字节范围" << endl;
    cout << "2. 性能优化：使用位运算提高效率" << endl;
    cout << "3. 可读性：清晰的变量命名和注释" << endl;
    cout << "4. 错误处理：详细的错误信息（实际工程中）" << endl;
    
    // 算法技巧总结
    cout << "\n=== 算法技巧总结 ===" << endl;
    cout << "1. 位掩码：使用位掩码检查字节格式" << endl;
    cout << "2. 状态机：跟踪当前字符的字节数" << endl;
    cout << "3. 提前终止：发现无效字节立即返回" << endl;
    cout << "4. 边界检查：确保不越界访问数组" << endl;
    
    // UTF-8编码规则总结
    cout << "\n=== UTF-8编码规则 ===" << endl;
    cout << "1字节: 0xxxxxxx" << endl;
    cout << "2字节: 110xxxxx 10xxxxxx" << endl;
    cout << "3字节: 1110xxxx 10xxxxxx 10xxxxxx" << endl;
    cout << "4字节: 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx" << endl;
    
    return 0;
}