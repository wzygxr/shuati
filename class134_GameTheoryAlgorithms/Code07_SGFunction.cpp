// SG函数 (Sprague-Grundy定理) 实现
// 公平组合游戏(Impartial Game)的通用解法
// 任何公平组合游戏都可以转化为尼姆堆，通过计算每个子游戏的SG值
// 然后将这些SG值异或起来，若结果非零则先手必胜，否则必败
// 
// 算法思路：
// 1. SG函数是对游戏状态的一种抽象表示
// 2. 对于每个状态x，SG(x) = mex{ SG(y) | y是x的后继状态 }
// 3. mex(最小非负整数)函数返回不属于集合中的最小非负整数
// 4. Sprague-Grundy定理：多个独立的子游戏的组合的SG值等于各子游戏SG值的异或和
// 5. 当且仅当组合游戏的SG值不为0时，当前玩家处于必胜态
// 
// 时间复杂度：O(n * m) - n是状态数，m是每个状态的后继状态数
// 空间复杂度：O(n) - 存储SG值的数组
//
// 适用场景和解题技巧：
// 1. 适用场景：
//    - 公平组合游戏（双方可执行相同操作，游戏状态无差别）
//    - 有确定的终止状态
//    - 每个状态可以转移到有限个其他状态
// 2. 解题技巧：
//    - 确定游戏的状态表示方法
//    - 找出每个状态的所有可能转移
//    - 自底向上计算SG函数值
//    - 利用异或和判断胜负
// 3. 经典应用：
//    - 取石子游戏的变种
//    - 棋盘游戏
//    - 图游戏
//
// 相关题目链接：
// 1. 洛谷 P2197: https://www.luogu.com.cn/problem/P2197
// 2. HDU 1850: http://acm.hdu.edu.cn/showproblem.php?pid=1850
// 3. POJ 2234: http://poj.org/problem?id=2234

// 简化版本，不使用标准库中的复杂功能
// 由于编译环境问题，避免使用<iostream>等标准头文件

// 预处理SG函数值
// 参数说明：
// - n: 最大状态数
// - moves: 可以进行的移动数组
// - movesSize: 移动数组的大小
// - sg: 存储计算结果的数组
void precomputeSG(int n, int* moves, int movesSize, int* sg) {
    /**
     * 预处理SG函数值
     * 
     * 参数说明：
     * - n: 最大状态数
     * - moves: 可以进行的移动数组
     * - movesSize: 移动数组的大小
     * - sg: 存储计算结果的数组
     * 
     * 算法思路：
     * 1. 对于每个状态i，计算其所有后继状态的SG值
     * 2. 使用mex函数找出最小的不属于后继状态SG值集合的非负整数
     * 3. 该值即为状态i的SG值
     * 
     * 时间复杂度：O(n * m) - n是状态数，m是每个状态的后继状态数
     * 空间复杂度：O(n) - 存储SG值的数组
     */
    // 初始化标记数组
    char* visited = new char[n + 1];
    
    // 自底向上计算每个状态的SG值
    for (int i = 1; i <= n; i++) {
        // 重置标记数组
        for (int j = 0; j <= n; j++) {
            visited[j] = 0;
        }
        
        // 遍历所有可能的移动
        for (int j = 0; j < movesSize; j++) {
            if (i >= moves[j]) {
                visited[sg[i - moves[j]]] = 1;
            }
        }
        
        // 计算mex值
        int mex = 0;
        while (visited[mex]) {
            mex++;
        }
        sg[i] = mex;
    }
    
    delete[] visited;
}

// 判断当前玩家是否必胜
// 参数说明：
// - piles: 各堆石子的数量数组
// - pilesSize: 数组大小
// - moves: 可以进行的移动数组
// - movesSize: 移动数组的大小
char isWinningPosition(int* piles, int pilesSize, int* moves, int movesSize) {
    /**
     * 判断当前玩家是否必胜
     * 
     * 参数说明：
     * - piles: 各堆石子的数量数组
     * - pilesSize: 数组大小
     * - moves: 可以进行的移动数组
     * - movesSize: 移动数组的大小
     * 
     * 返回:
     * - 'Y'表示当前玩家必胜，'N'表示必败
     * 
     * 算法思路：
     * 1. 预处理SG函数值到最大堆的大小
     * 2. 计算所有堆的SG值异或和
     * 3. 异或和不为0则先手必胜，否则必败
     * 
     * 时间复杂度：O(n * m) - n是状态数，m是每个状态的后继状态数
     * 空间复杂度：O(n) - 存储SG值的数组
     */
    // 找出最大堆的大小
    int maxPile = 0;
    for (int i = 0; i < pilesSize; i++) {
        if (piles[i] > maxPile) {
            maxPile = piles[i];
        }
    }
    
    // 分配SG数组
    int* sg = new int[maxPile + 1];
    for (int i = 0; i <= maxPile; i++) {
        sg[i] = 0;
    }
    
    // 预处理SG函数值
    precomputeSG(maxPile, moves, movesSize, sg);
    
    // 计算所有堆的SG值异或和
    int xorSum = 0;
    for (int i = 0; i < pilesSize; i++) {
        xorSum ^= sg[piles[i]];
    }
    
    delete[] sg;
    
    // 异或和不为0则先手必胜，返回'Y'；否则返回'N'
    return (xorSum != 0) ? 'Y' : 'N';
}

// 为了测试和验证，提供一个简单的主函数示例
// 注意：在实际应用中，需要根据具体的输入输出要求修改
int main() {
    // 这里仅作为示例，实际使用时需要根据题目要求读取输入
    
    // 测试用例1: 巴什博弈变种 - 每次可以取1、2、4个石子
    int moves1[] = {1, 2, 4};
    int piles1[] = {5, 7, 9};
    char result1 = isWinningPosition(piles1, 3, moves1, 3);
    
    // 测试用例2: 标准巴什博弈 - 每次可以取1-3个石子
    int moves2[] = {1, 2, 3};
    int piles2[] = {4, 4, 4};
    char result2 = isWinningPosition(piles2, 3, moves2, 3);
    
    // 测试用例3: 斐波那契游戏的SG函数分析
    int moves3[] = {1, 2};
    int piles3[] = {5};
    char result3 = isWinningPosition(piles3, 1, moves3, 2);
    
    // 由于编译环境限制，这里不使用printf输出
    // 在实际应用中，可以根据需要添加输出语句
    
    return 0;
}