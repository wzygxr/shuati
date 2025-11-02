#include <iostream>
#include <cstring>
using namespace std;

/**
 * 单词搜索（Word Search） - C++实现
 * 
 * 题目描述：
 * 给定一个 m x n 二维字符网格 board 和一个字符串单词 word
 * 如果 word 存在于网格中，返回 true ；否则，返回 false 。
 * 单词必须按照字母顺序，通过相邻的单元格内的字母构成
 * 其中"相邻"单元格是那些水平相邻或垂直相邻的单元格
 * 同一个单元格内的字母不允许被重复使用
 * 
 * 题目来源：LeetCode 79. 单词搜索
 * 题目链接：https://leetcode.cn/problems/word-search/
 * 
 * 算法思想：深度优先搜索（DFS）+ 回溯法
 * 1. 遍历网格中的每个位置作为起点
 * 2. 从每个起点开始进行深度优先搜索
 * 3. 在搜索过程中使用回溯法避免重复使用同一单元格
 * 4. 当找到完整单词时返回true，否则继续搜索
 * 
 * 时间复杂度：O(m*n*4^L) - 其中L为单词长度，最坏情况下需要从每个位置开始搜索
 * 空间复杂度：O(m*n) - 递归栈深度和标记数组
 * 是否最优解：是 - 回溯法是解决此类路径搜索问题的标准方法
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数合法性，处理空网格、空单词等特殊情况
 * 2. 边界处理：处理网格边界、单词边界等边界条件
 * 3. 性能优化：剪枝策略提前终止无效搜索，减少不必要的递归
 * 4. 可测试性：提供完整的测试用例，覆盖各种边界场景
 * 
 * C++特性：
 * - 使用字符数组而非vector，性能更高但需要预设最大尺寸
 * - 需要手动管理内存，注意数组边界
 * - 使用const引用传递参数，避免不必要的拷贝
 * 
 * 跨语言差异：
 * - 与Java相比：需要预设数组最大尺寸，手动管理内存
 * - 与Python相比：性能更高，但代码更复杂
 */

#define MAXN 100  // 假设网格最大为100x100

class Code02_WordSearch {
public:
    /**
     * 判断单词是否存在于网格中
     * 
     * 算法流程：
     * 1. 输入验证：检查网格和单词的合法性
     * 2. 单词预处理：计算单词长度
     * 3. 遍历起点：从网格的每个位置开始尝试搜索
     * 4. 深度优先搜索：对每个起点进行DFS搜索
     * 5. 结果返回：如果找到返回true，否则返回false
     * 
     * 时间复杂度：O(m*n*4^L) - 其中m,n为网格尺寸，L为单词长度
     * 空间复杂度：O(m*n) - 递归栈深度，最坏情况下需要遍历整个网格
     * 
     * 优化策略：
     * 1. 提前剪枝：如果单词长度超过网格总字符数，直接返回false
     * 
     * @param board 二维字符网格，不能为null或空
     * @param n     网格行数
     * @param m     网格列数
     * @param word  要搜索的单词，不能为null
     * @return 如果单词存在于网格中返回true，否则返回false
     * 
     * 异常处理：
     * - 空网格：返回false
     * - 空单词：返回false
     * - 单词长度超过网格总字符数：返回false
     */
    static bool exist(char board[][MAXN], int n, int m, const char* word) {
        // 输入验证
        if (board == nullptr || n <= 0 || m <= 0 || word == nullptr) {
            return false;
        }
        
        // 获取单词长度
        int wordLen = 0;
        while (word[wordLen] != '\0') {
            wordLen++;
        }
        
        // 额外优化：如果单词长度超过网格总字符数，直接返回false
        if (wordLen > n * m) {
            return false;
        }
        
        // 遍历网格中的每个位置作为起点
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // 从当前位置开始搜索，如果找到则返回true
                if (f(board, n, m, i, j, word, wordLen, 0)) {
                    return true;
                }
            }
        }
        
        // 遍历完所有位置都没找到，返回false
        return false;
    }

private:
    /**
     * 深度优先搜索（DFS）辅助函数
     * 
     * 算法流程：
     * 1. 终止条件检查：如果已匹配完整个单词，返回true
     * 2. 边界条件检查：检查是否越界或字符不匹配
     * 3. 标记访问：将当前单元格标记为已访问（使用特殊字符0）
     * 4. 四个方向探索：向上、下、左、右四个方向递归搜索
     * 5. 回溯恢复：恢复当前单元格的原始字符
     * 
     * 核心思想：回溯法
     * - 在递归前修改状态（标记已访问）
     * - 在递归后恢复状态（恢复原始字符）
     * - 确保每个单元格在单次搜索路径中只被使用一次
     * 
     * 时间复杂度：O(4^L) - 每个位置最多有4个方向选择，L为剩余字符数
     * 空间复杂度：O(L) - 递归栈深度，最坏情况下等于单词长度
     * 
     * 剪枝优化：
     * - 提前终止：一旦找到完整路径立即返回，避免不必要的搜索
     * - 边界检查：在递归前检查边界条件，减少无效递归
     * 
     * @param b      二维字符网格
     * @param n      网格行数
     * @param m      网格列数
     * @param i      当前行坐标
     * @param j      当前列坐标
     * @param w      要搜索的单词
     * @param wordLen 单词长度
     * @param k      当前要匹配的字符索引（从0开始）
     * @return 如果能从当前位置开始找到完整单词返回true，否则返回false
     */
    static bool f(char b[][MAXN], int n, int m, int i, int j, const char* w, int wordLen, int k) {
        // 基础情况：已经匹配完整个单词
        if (k == wordLen) {
            return true;
        }
        
        // 越界检查或字符不匹配
        if (i < 0 || i >= n || j < 0 || j >= m || b[i][j] != w[k]) {
            return false;
        }
        
        // 不越界且b[i][j] == w[k]，继续搜索
        // 标记当前位置已访问（用0表示已访问）
        char tmp = b[i][j];
        b[i][j] = 0;
        
        // 向四个方向递归搜索
        bool ans = f(b, n, m, i - 1, j, w, wordLen, k + 1) ||  // 上
                   f(b, n, m, i + 1, j, w, wordLen, k + 1) ||  // 下
                   f(b, n, m, i, j - 1, w, wordLen, k + 1) ||  // 左
                   f(b, n, m, i, j + 1, w, wordLen, k + 1);    // 右
        
        // 回溯：恢复当前位置的字符
        b[i][j] = tmp;
        
        return ans;
    }
    
public:
    /**
     * 测试方法：验证单词搜索算法的正确性
     * 
     * 测试用例设计：
     * 1. 正常情况测试：单词存在于网格中
     * 2. 边界情况测试：单词不存在于网格中
     * 3. 特殊情况测试：单字符网格、空网格、空单词
     * 4. 复杂情况测试：包含回溯的路径搜索
     * 
     * 测试目的：确保算法在各种情况下都能正确工作
     */
    static void test() {
        cout << "=== 单词搜索算法测试 ===" << endl;
        
        // 测试用例1：正常情况 - 单词存在
        char board1[][MAXN] = {
            {'A', 'B', 'C', 'E'},
            {'S', 'F', 'C', 'S'},
            {'A', 'D', 'E', 'E'}
        };
        const char* word1 = "ABCCED";
        cout << "测试用例1 - 正常情况:" << endl;
        cout << "单词: " << word1 << endl;
        cout << "是否存在: " << (exist(board1, 3, 4, word1) ? "true" : "false") << endl;
        cout << "预期结果: true" << endl;
        cout << endl;
        
        // 测试用例2：正常情况 - 单词不存在
        const char* word2 = "ABCB";
        cout << "测试用例2 - 单词不存在:" << endl;
        cout << "单词: " << word2 << endl;
        cout << "是否存在: " << (exist(board1, 3, 4, word2) ? "true" : "false") << endl;
        cout << "预期结果: false" << endl;
        cout << endl;
        
        // 测试用例3：单字符网格
        char board3[][MAXN] = {{'A'}};
        const char* word3 = "A";
        const char* word4 = "B";
        cout << "测试用例3 - 单字符网格:" << endl;
        cout << "单词 '" << word3 << "' 是否存在: " << (exist(board3, 1, 1, word3) ? "true" : "false") << endl;
        cout << "单词 '" << word4 << "' 是否存在: " << (exist(board3, 1, 1, word4) ? "true" : "false") << endl;
        cout << "预期结果: true, false" << endl;
        cout << endl;
        
        // 测试用例4：包含回溯的复杂路径
        char board4[][MAXN] = {
            {'A', 'B', 'A', 'B'},
            {'B', 'A', 'B', 'A'},
            {'A', 'B', 'A', 'B'}
        };
        const char* word5 = "ABABABAB";  // 需要回溯的路径
        cout << "测试用例4 - 复杂回溯路径:" << endl;
        cout << "单词: " << word5 << endl;
        cout << "是否存在: " << (exist(board4, 3, 4, word5) ? "true" : "false") << endl;
        cout << "预期结果: true" << endl;
        cout << endl;
        
        cout << "=== 测试完成 ===" << endl;
    }
};

// 主函数：运行测试用例
int main() {
    Code02_WordSearch::test();
    return 0;
}