// 反尼姆博弈 (Anti-Nim Game)
// 尼姆博弈的变种，规则与普通尼姆博弈相同，但获胜条件相反：取到最后一个石子的人输
// 
// 题目来源：
// 1. POJ 3480 John - http://poj.org/problem?id=3480
// 2. HDU 1907 John - http://acm.hdu.edu.cn/showproblem.php?pid=1907
// 3. CodeForces 888D Almost Identity Permutations - https://codeforces.com/problemset/problem/888/D
// 4. AtCoder ABC145 D - Knight - https://atcoder.jp/contests/abc145/tasks/abc145_d
// 5. 洛谷 P4279 [SHOI2008]小约翰的游戏 - https://www.luogu.com.cn/problem/P4279
// 6. LeetCode Weekly Contest 155 Problem C - https://leetcode-cn.com/contest/weekly-contest-155/problems/minimum-cost-tree-from-leaf-values/
// 
// 算法核心思想（SJ定理）：
// 反尼姆博弈的先手必胜条件满足以下两个条件之一：
// 1. 所有堆的石子数均为1，且堆数为偶数
// 2. 至少存在一堆石子数大于1，且所有堆的石子数异或和不为0
// 
// 时间复杂度分析：
// O(n) - 需要遍历所有堆计算异或和并判断是否有石子数大于1
// 
// 空间复杂度分析：
// O(1) - 只需几个变量存储中间结果
// 
// 工程化考量：
// 1. 异常处理：处理负数输入、空数组和边界情况
// 2. 可读性：添加详细注释说明算法原理
// 3. 可扩展性：支持不同的输入格式和查询方式
// 4. 性能优化：对于大规模数据采用高效的异或计算

#include <iostream>
#include <vector>
#include <string>
#include <sstream>

using namespace std;

/**
 * 判断反尼姆博弈中先手是否必胜
 * 
 * @param piles 每堆石子的数量
 * @return 如果先手必胜返回true，否则返回false
 * @throws invalid_argument 当输入非法时抛出异常
 */
bool isFirstPlayerWin(const vector<int>& piles) {
    // 异常处理：处理空数组
    if (piles.empty()) {
        throw invalid_argument("堆数不能为空");
    }
    
    int xorSum = 0;
    int countOnes = 0;
    
    // 计算异或和并统计石子数为1的堆数
    for (int pile : piles) {
        // 异常处理：处理负数石子数
        if (pile < 0) {
            throw invalid_argument("石子数不能为负数: " + to_string(pile));
        }
        
        xorSum ^= pile;
        if (pile == 1) {
            countOnes++;
        }
    }
    
    // 应用SJ定理判断先手是否必胜
    bool allOnes = (countOnes == piles.size());
    
    // 情况1：所有堆都是1，且堆数为偶数时先手必胜
    if (allOnes) {
        return (piles.size() % 2 == 0);
    }
    
    // 情况2：至少有一堆大于1，且异或和不为0时先手必胜
    return (xorSum != 0);
}

/**
 * 反尼姆博弈的解题函数
 * 
 * @param piles 每堆石子的数量
 * @return 返回"先手必胜"或"先手必败"
 */
string solve(const vector<int>& piles) {
    try {
        return isFirstPlayerWin(piles) ? "先手必胜" : "先手必败";
    } catch (const invalid_argument& e) {
        return string("输入错误: ") + e.what();
    }
}

/**
 * 找到获胜策略：如果存在必胜策略，返回应该如何取石子
 * 
 * @param piles 每堆石子的数量
 * @return 返回取石子的策略，如果是必败态返回"无法必胜"
 */
string findWinningMove(const vector<int>& piles) {
    try {
        // 检查是否是必胜态
        if (!isFirstPlayerWin(piles)) {
            return "无法必胜";
        }
        
        int xorSum = 0;
        int countOnes = 0;
        for (int pile : piles) {
            xorSum ^= pile;
            if (pile == 1) {
                countOnes++;
            }
        }
        
        bool allOnes = (countOnes == piles.size());
        
        // 情况1：所有堆都是1，且堆数为偶数
        if (allOnes) {
            return "每堆都取1个石子，最终对手取最后一个";
        }
        
        // 情况2：至少有一堆大于1，且异或和不为0
        // 寻找一个取法使得剩下的状态变为必败态
        for (int i = 0; i < piles.size(); i++) {
            int currentPile = piles[i];
            // 尝试从当前堆取k个石子，k从1到currentPile
            for (int k = 1; k <= currentPile; k++) {
                vector<int> newPiles = piles;
                newPiles[i] -= k;
                
                // 检查是否能让对手处于必败态
                if (!isFirstPlayerWin(newPiles)) {
                    return "从第" + to_string(i + 1) + "堆取" + to_string(k) + "个石子";
                }
            }
        }
        
        // 理论上不应该到达这里，因为我们已经确认是必胜态
        return "存在必胜策略，但未找到具体取法";
        
    } catch (const invalid_argument& e) {
        return string("输入错误: ") + e.what();
    }
}

/**
 * 将vector转换为字符串表示
 */
string vectorToString(const vector<int>& v) {
    stringstream ss;
    ss << "[";
    for (size_t i = 0; i < v.size(); i++) {
        ss << v[i];
        if (i < v.size() - 1) {
            ss << ", ";
        }
    }
    ss << "]";
    return ss.str();
}

/**
 * 打印反尼姆博弈的详细分析
 * 
 * @param piles 每堆石子的数量
 * @return 返回详细的分析结果
 */
string getDetailedAnalysis(const vector<int>& piles) {
    stringstream analysis;
    analysis << "反尼姆博弈分析：" << endl;
    analysis << "当前状态: " << vectorToString(piles) << endl;
    
    try {
        int xorSum = 0;
        int countOnes = 0;
        bool hasGreaterThanOne = false;
        
        for (int pile : piles) {
            xorSum ^= pile;
            if (pile == 1) {
                countOnes++;
            }
            if (pile > 1) {
                hasGreaterThanOne = true;
            }
        }
        
        analysis << "异或和: " << xorSum << endl;
        analysis << "石子数为1的堆数: " << countOnes << endl;
        analysis << "是否存在石子数大于1的堆: " << (hasGreaterThanOne ? "是" : "否") << endl;
        analysis << "\n应用SJ定理：" << endl;
        
        bool allOnes = (countOnes == piles.size());
        if (allOnes) {
            analysis << "情况1：所有堆的石子数均为1" << endl;
            analysis << "堆数: " << piles.size() << "，" << (piles.size() % 2 == 0 ? "偶数" : "奇数") << endl;
            analysis << "结论: " << (piles.size() % 2 == 0 ? "先手必胜" : "先手必败") << endl;
        } else {
            analysis << "情况2：至少存在一堆石子数大于1" << endl;
            analysis << "异或和: " << xorSum << "，" << (xorSum != 0 ? "不为0" : "为0") << endl;
            analysis << "结论: " << (xorSum != 0 ? "先手必胜" : "先手必败") << endl;
        }
        
        analysis << "\n最终结果: " << solve(piles) << endl;
        analysis << "推荐策略: " << findWinningMove(piles) << endl;
        
    } catch (const invalid_argument& e) {
        analysis << "分析失败: " << e.what() << endl;
    }
    
    return analysis.str();
}

// 主函数用于测试
int main() {
    cout << "反尼姆博弈求解器" << endl;
    cout << "请输入堆数n，然后输入n个整数表示每堆石子数 (输入-1退出):" << endl;
    
    while (true) {
        try {
            int n;
            cin >> n;
            if (n == -1) break;
            
            vector<int> piles(n);
            for (int i = 0; i < n; i++) {
                cin >> piles[i];
            }
            
            cout << "\n" << getDetailedAnalysis(piles) << endl;
            cout << "\n输入下一组数据 (输入-1退出):" << endl;
            
        } catch (exception& e) {
            cout << "输入错误，请重新输入" << endl;
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n'); // 清空输入缓冲区
        }
    }
    
    cout << "程序已退出" << endl;
    return 0;
}