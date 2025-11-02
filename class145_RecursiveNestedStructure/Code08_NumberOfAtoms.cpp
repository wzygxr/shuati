// LeetCode 726. Number of Atoms (原子的数量)
// 题目来源：https://leetcode.cn/problems/number-of-atoms/
//
// 题目描述：
// 给定一个化学式formula（作为字符串），返回每种原子的数量。
// 原子总是以一个大写字母开始，接着跟随0个或任意个小写字母，表示原子的名字。
// 如果数量大于 1，原子后会跟着数字表示原子的数量。如果数量等于 1 则不会跟数字。
// 例如，H2O 和 H2O2 是可行的，但 H1O2 这个表达是不可行的。
// 两个化学式连在一起是新的化学式。例如 H2O2He3Mg4 也是化学式。
// 一个括号中的化学式和数字（可选择性添加）也是化学式。例如 (H2O2) 和 (H2O2)3 是化学式。
// 返回所有原子的数量，格式为：按原子名排序，后面跟着数量（如果数量大于1）。
//
// 解题思路：
// 使用栈处理嵌套结构，遇到左括号时入栈，遇到右括号时出栈并乘以倍数
// 用map存储原子名称和对应的数量，最后按字典序输出
//
// 时间复杂度：O(n log k)，其中n是字符串长度，k是不同原子的数量
// 空间复杂度：O(n + k)，栈的空间和map的空间

#include <iostream>
#include <string>
#include <map>
#include <stack>
#include <cctype>
using namespace std;

class Solution {
public:
    string countOfAtoms(string formula) {
        stack<map<string, int>> stk;
        map<string, int> current;
        int n = formula.size();
        int i = 0;
        
        while (i < n) {
            if (formula[i] == '(') {
                // 遇到左括号，将当前map入栈，开始新的map
                stk.push(current);
                current = map<string, int>();
                i++;
            } else if (formula[i] == ')') {
                // 遇到右括号，处理括号内的倍数
                i++;
                int multiplier = 0;
                while (i < n && isdigit(formula[i])) {
                    multiplier = multiplier * 10 + (formula[i] - '0');
                    i++;
                }
                multiplier = multiplier == 0 ? 1 : multiplier;
                
                // 将当前map中的原子数量乘以倍数，然后与栈顶map合并
                for (auto& pair : current) {
                    current[pair.first] = pair.second * multiplier;
                }
                
                if (!stk.empty()) {
                    map<string, int> top = stk.top();
                    stk.pop();
                    for (auto& pair : current) {
                        top[pair.first] += pair.second;
                    }
                    current = top;
                }
            } else {
                // 处理原子名称和数量
                // 提取原子名称
                string atomName = "";
                atomName += formula[i++];
                while (i < n && islower(formula[i])) {
                    atomName += formula[i++];
                }
                
                // 提取原子数量
                int count = 0;
                while (i < n && isdigit(formula[i])) {
                    count = count * 10 + (formula[i] - '0');
                    i++;
                }
                count = count == 0 ? 1 : count;
                
                current[atomName] += count;
            }
        }
        
        // 构建结果字符串
        string result = "";
        for (auto& pair : current) {
            result += pair.first;
            if (pair.second > 1) {
                result += to_string(pair.second);
            }
        }
        
        return result;
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    string formula1 = "H2O";
    cout << "输入: " << formula1 << endl;
    cout << "输出: " << solution.countOfAtoms(formula1) << endl;
    cout << "期望: H2O" << endl << endl;
    
    // 测试用例2
    string formula2 = "Mg(OH)2";
    cout << "输入: " << formula2 << endl;
    cout << "输出: " << solution.countOfAtoms(formula2) << endl;
    cout << "期望: H2MgO2" << endl << endl;
    
    // 测试用例3
    string formula3 = "K4(ON(SO3)2)2";
    cout << "输入: " << formula3 << endl;
    cout << "输出: " << solution.countOfAtoms(formula3) << endl;
    cout << "期望: K4N2O14S4" << endl << endl;
    
    return 0;
}

/**
 * 算法分析：
 * 
 * 时间复杂度分析：
 * - 每个字符最多被处理一次，因此时间复杂度为O(n)
 * - map操作的时间复杂度为O(log k)，其中k是不同原子的数量
 * - 总时间复杂度为O(n log k)
 * 
 * 空间复杂度分析：
 * - 栈的空间复杂度为O(n)
 * - map存储原子数量的空间复杂度为O(k)
 * - 总空间复杂度为O(n + k)
 * 
 * 算法优化思路：
 * 1. 使用unordered_map可以降低查找时间复杂度到O(1)，但需要额外排序
 * 2. 可以优化字符串处理，使用更高效的数据结构
 * 3. 可以优化数字构建过程，避免重复计算
 * 
 * 工程化考量：
 * 1. 异常处理：添加对非法化学式的检查
 * 2. 边界条件：处理空字符串、单个原子等特殊情况
 * 3. 性能优化：对于大规模化学式，可以考虑使用更高效的数据结构
 * 4. 内存管理：在C++中需要注意map的内存使用
 * 
 * 相关题目对比：
 * 1. LeetCode 394. Decode String：解码字符串而不是统计原子数量
 * 2. LeetCode 772. Basic Calculator III：计算表达式而不是统计原子数量
 * 3. LeetCode 856. Score of Parentheses：计算括号的分数而不是统计原子数量
 * 
 * 算法应用场景：
 * 1. 化学式解析器
 * 2. 分子量计算
 * 3. 化学方程式配平
 * 4. 化学数据库查询
 * 
 * C++语言特性利用：
 * 1. 使用map自动维护键的顺序，避免手动排序
 * 2. 使用stack处理嵌套结构
 * 3. 使用isdigit和islower函数判断字符类型
 * 4. 利用RAII原则自动管理资源
 */