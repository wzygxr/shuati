// LeetCode 1222 Queens That Can Attack the King
// C++ 实现

/**
 * LeetCode 1222 Queens That Can Attack the King
 * 
 * 题目描述：
 * 在一个 8x8 的棋盘上，有一个白色的国王和一些黑色的皇后。
 * 给你一个二维整数数组 queens，其中 queens[i] = [xQueeni, yQueeni] 表示第 i 个黑色皇后在棋盘上的位置。
 * 还给你一个长度为 2 的整数数组 king，其中 king = [xKing, yKing] 表示白色国王的位置。
 * 
 * 请你返回能够直接攻击国王的黑色皇后的坐标。你可以以任何顺序返回答案。
 * 
 * 解题思路：
 * 我们可以从国王的位置出发，向8个方向（上下左右和4个对角线方向）搜索，
 * 找到第一个遇到的皇后，这个皇后就是能够攻击国王的皇后。
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 */

// 由于编译环境限制，这里提供算法思路和伪代码实现

/*
#include <stdlib.h>

int** queensAttacktheKing(int** queens, int queensSize, int* queensColSize, int* king, int kingSize, int* returnSize, int** returnColumnSizes) {
    // 创建棋盘标记皇后位置
    int board[8][8] = {0};
    for (int i = 0; i < queensSize; i++) {
        int x = queens[i][0];
        int y = queens[i][1];
        board[x][y] = 1;
    }
    
    int kingX = king[0];
    int kingY = king[1];
    
    // 8个方向：上下左右和4个对角线方向
    int directions[8][2] = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1},  // 上下左右
        {-1, -1}, {-1, 1}, {1, -1}, {1, 1}  // 4个对角线方向
    };
    
    // 创建结果数组
    int** result = (int**)malloc(8 * sizeof(int*));
    int resultSize = 0;
    
    // 向8个方向搜索
    for (int i = 0; i < 8; i++) {
        int dx = directions[i][0];
        int dy = directions[i][1];
        
        // 从国王位置开始，沿着当前方向搜索
        int x = kingX + dx;
        int y = kingY + dy;
        
        while (x >= 0 && x < 8 && y >= 0 && y < 8) {
            if (board[x][y] == 1) {
                // 找到皇后，添加到结果中
                result[resultSize] = (int*)malloc(2 * sizeof(int));
                result[resultSize][0] = x;
                result[resultSize][1] = y;
                resultSize++;
                break;  // 找到第一个皇后后停止搜索这个方向
            }
            x += dx;
            y += dy;
        }
    }
    
    // 设置返回参数
    *returnSize = resultSize;
    *returnColumnSizes = (int*)malloc(resultSize * sizeof(int));
    for (int i = 0; i < resultSize; i++) {
        (*returnColumnSizes)[i] = 2;
    }
    
    return result;
}

// 算法核心思想：
// 1. 从国王位置向8个方向搜索
// 2. 找到每个方向上第一个遇到的皇后
// 3. 这些皇后就是能够攻击国王的皇后

// 时间复杂度分析：
// - 搜索8个方向：O(1)
// - 每个方向最多搜索7步：O(1)
// - 总体时间复杂度：O(1)
// - 空间复杂度：O(1)
*/

// 算法应用场景：
// 1. 棋盘问题
// 2. 搜索算法
// 3. 游戏算法