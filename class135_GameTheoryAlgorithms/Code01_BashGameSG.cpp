// 巴什博弈(SG函数求解过程展示)
// 一共有n颗石子，两个人轮流拿，每次可以拿1~m颗石子
// 拿到最后一颗石子的人获胜，根据n、m返回谁赢
// 对数器验证

// 题目来源：
// 1. 洛谷 P1247 取火柴游戏 - https://www.luogu.com.cn/problem/P1247
// 2. LeetCode 292. Nim Game - https://leetcode.com/problems/nim-game/
// 3. 牛客网 NC13685 取石子游戏 - https://www.nowcoder.com/practice/f6153503169545229c77481040056a63
// 4. HDU 1846 Brave Game - http://acm.hdu.edu.cn/showproblem.php?pid=1846
// 5. POJ 2313. Bash Game - http://poj.org/problem?id=2313

// 算法核心思想：
// 1. SG函数方法：通过递推计算每个状态的SG值，SG值不为0表示必胜态，为0表示必败态
// 2. 数学规律方法：通过数学推导发现规律，n%(m+1)!=0时先手必胜，否则后手必胜

// 时间复杂度分析：
// 1. bash1方法：O(1) - 直接使用数学公式
// 2. bash2方法：O(n*m) - 需要计算每个状态的SG值

// 空间复杂度分析：
// 1. bash1方法：O(1) - 只需要常数空间
// 2. bash2方法：O(n) - 需要存储SG值数组

// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：bash1方法是数学规律的最优解
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：bash2方法展示了SG函数的通用求解过程

// SG函数原理：
// SG函数是博弈论中用于解决公平组合游戏的重要工具，其定义为：
// SG(x) = mex{SG(y) | y是x的后继状态}
// 其中mex(S)表示不属于集合S的最小非负整数

// 对于巴什博弈，SG函数的计算过程：
// 1. 终止状态SG值为0（没有石子时）
// 2. 对于状态i，其后继状态为i-1, i-2, ..., i-m（如果存在）
// 3. SG(i) = mex{SG(i-1), SG(i-2), ..., SG(i-m)}

// 通过打表可以发现规律：SG(i) = i % (m+1)
// 这就是bash1方法的数学基础

class Code01_BashGameSG {
public:
    // 发现结论去求解，时间复杂度O(1)
    // 充分研究了性质
    // 
    // 算法原理：
    // 在巴什博弈中，如果石子总数n是(m+1)的倍数，那么后手必胜
    // 否则先手必胜
    // 
    // 证明思路：
    // 1. 当n%(m+1)=0时，无论先手取k(1<=k<=m)个石子
    // 2. 后手总能取(m+1-k)个石子，使得剩余石子数仍为(m+1)的倍数
    // 3. 最终后手取走最后的石子获胜
    // 
    // 反之，当n%(m+1)!=0时，先手可以取走(n%(m+1))个石子
    // 使得剩余石子数为(m+1)的倍数，转化为后手必败态
    static const char* bash1(int n, int m) {
        // 异常处理：处理非法输入
        if (n < 0 || m <= 0) {
            return "输入非法";
        }
        
        // 核心判断逻辑
        return n % (m + 1) != 0 ? "先手" : "后手";
    }

    // sg函数去求解，时间复杂度O(n*m)
    // 不用研究性质
    // 其实把sg表打印之后，也可以发现性质，也就是打表找规律
    // 
    // 算法原理：
    // 通过递推计算每个状态的SG值
    // 1. SG[0] = 0（没有石子，必败态）
    // 2. 对于状态i，其后继状态为i-j (1<=j<=m且i-j>=0)
    // 3. SG[i] = mex{SG[i-1], SG[i-2], ..., SG[i-m]}
    static const char* bash2(int n, int m) {
        // 异常处理：处理非法输入
        if (n < 0 || m <= 0) {
            return "输入非法";
        }
        
        // 使用简单数组代替vector
        int* sg = new int[n + 1];
        bool* appear = new bool[m + 1];
        
        // 初始化
        for (int i = 0; i <= n; i++) {
            sg[i] = 0;
        }
        
        // 递推计算每个状态的SG值
        for (int i = 1; i <= n; i++) {
            // 初始化appear数组
            for (int k = 0; k <= m; k++) {
                appear[k] = false;
            }
            
            // 计算状态i的所有后继状态的SG值
            for (int j = 1; j <= m && i - j >= 0; j++) {
                // 标记后继状态的SG值已出现
                appear[sg[i - j]] = true;
            }
            
            // 计算mex值，即不属于appear集合的最小非负整数
            for (int s = 0; s <= m; s++) {
                if (!appear[s]) {
                    sg[i] = s;
                    break;
                }
            }
        }

        // SG值不为0表示必胜态，为0表示必败态
        bool result = sg[n] != 0;
        
        // 释放内存
        delete[] sg;
        delete[] appear;
        
        return result ? "先手" : "后手";
    }
};