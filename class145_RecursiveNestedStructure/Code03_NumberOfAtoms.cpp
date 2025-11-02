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
// 使用递归处理嵌套结构，遇到左括号时递归处理括号内的化学式
// 用全局变量where记录当前处理到的位置，用于递归返回时告知上游函数从哪继续处理
// 用map存储原子名称和对应的数量，保证输出时按字典序排列
//
// 时间复杂度：O(n)，其中n是字符串的长度
// 空间复杂度：O(n)，递归调用栈的深度和存储原子数量的额外空间

#include <iostream>
#include <string>
#include <map>
#include <vector>
using namespace std;

class Solution {
public:
    int where; // 全局变量，记录当前处理到的位置，用于递归返回时告知上游函数从哪继续处理
    
    string countOfAtoms(string formula) {
        where = 0;
        map<string, int> atomMap = f(formula, 0);
        string result = "";
        
        for (auto& pair : atomMap) {
            result += pair.first;
            if (pair.second > 1) {
                result += to_string(pair.second);
            }
        }
        
        return result;
    }
    
    // s[i....]开始计算，遇到字符串终止 或者 遇到 ) 停止
    // 返回 : 自己负责的这一段字符串的结果，有序表！
    // 返回之间，更新全局变量where，为了上游函数知道从哪继续！
    map<string, int> f(string s, int i) {
        // ans是总表，存储原子名称和对应的数量
        map<string, int> ans;
        // 之前收集到的名字，历史一部分
        string name = "";
        // 之前收集到的有序表，历史一部分
        map<string, int> pre;
        // 历史翻几倍
        int cnt = 0;
        
        while (i < s.length() && s[i] != ')') {
            if ((s[i] >= 'A' && s[i] <= 'Z') || s[i] == '(') {
                fill(ans, name, pre, cnt);
                name = "";
                pre.clear();
                cnt = 0;
                
                if (s[i] >= 'A' && s[i] <= 'Z') {
                    name += s[i++];
                } else {
                    // 遇到 (
                    pre = f(s, i + 1);
                    i = where + 1;
                }
            } else if (s[i] >= 'a' && s[i] <= 'z') {
                name += s[i++];
            } else {
                cnt = cnt * 10 + s[i++] - '0';
            }
        }
        
        fill(ans, name, pre, cnt);
        where = i;
        return ans;
    }
    
    // 将收集到的原子信息填充到结果中
    void fill(map<string, int>& ans, string name, map<string, int>& pre, int cnt) {
        if (!name.empty() || !pre.empty()) {
            cnt = (cnt == 0) ? 1 : cnt;
            
            if (!name.empty()) {
                ans[name] += cnt;
            } else {
                for (auto& pair : pre) {
                    ans[pair.first] += pair.second * cnt;
                }
            }
        }
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
 * - 递归调用栈的深度为O(n)
 * - map存储原子数量的空间复杂度为O(k)
 * - 总空间复杂度为O(n + k)
 * 
 * 算法优化思路：
 * 1. 使用unordered_map可以降低查找时间复杂度到O(1)，但需要额外排序
 * 2. 使用迭代+栈的方法可以避免递归，减少栈空间的使用
 * 3. 可以优化字符串处理，使用更高效的数据结构
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
 * 2. 使用auto关键字简化迭代器操作
 * 3. 使用引用传递避免不必要的拷贝
 * 4. 利用RAII原则自动管理资源
 */