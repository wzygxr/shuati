/**
 * LeetCode 1483. 树节点的第 K 个祖先（树上倍增法）
 * 题目链接：https://leetcode.com/problems/kth-ancestor-of-a-tree-node/
 * 题目描述：给定一棵树，每个节点有唯一的父节点，实现一个类TreeAncestor：
 *   - TreeAncestor(int n, int[] parent): 初始化，n个节点，parent[i]是节点i的父节点
 *   - int getKthAncestor(int node, int k): 返回节点node的第k个祖先节点
 * 解法：树上倍增法（二进制拆分）
 * 
 * 算法思路：
 * 1. 使用倍增法预处理每个节点的2^i级祖先
 * 2. 对于查询getKthAncestor(node, k)，将k二进制拆分
 * 3. 从高位到低位，如果k的第i位为1，则node跳到其2^i级祖先
 * 4. 重复直到k为0或node为-1
 * 
 * 时间复杂度：
 *   - 初始化：O(N log N)
 *   - 查询：O(log K)
 * 空间复杂度：O(N log N)
 */

#include <iostream>
#include <vector>
#include <cmath>
using namespace std;

class TreeAncestor {
private:
    int n;
    int maxLevel;
    vector<vector<int>> stjump;
    
public:
    /**
     * 构造函数：初始化倍增数组
     * @param n 节点数量
     * @param parent 父节点数组，parent[i]是节点i的父节点
     */
    TreeAncestor(int n, vector<int>& parent) {
        this->n = n;
        // 计算最大层数：log2(n)
        this->maxLevel = 0;
        while ((1 << maxLevel) <= n) {
            maxLevel++;
        }
        
        // 初始化倍增数组
        stjump.resize(n, vector<int>(maxLevel, -1));
        
        // 初始化第0级祖先（直接父节点）
        for (int i = 0; i < n; i++) {
            stjump[i][0] = parent[i];
        }
        
        // 预处理倍增数组
        for (int j = 1; j < maxLevel; j++) {
            for (int i = 0; i < n; i++) {
                if (stjump[i][j-1] == -1) {
                    stjump[i][j] = -1;
                } else {
                    stjump[i][j] = stjump[stjump[i][j-1]][j-1];
                }
            }
        }
    }
    
    /**
     * 查询节点node的第k个祖先
     * @param node 当前节点
     * @param k 祖先级别
     * @return 第k个祖先节点，如果不存在返回-1
     */
    int getKthAncestor(int node, int k) {
        if (node < 0 || node >= n || k < 0) {
            return -1;
        }
        
        // 二进制拆分k
        for (int j = 0; j < maxLevel; j++) {
            if ((k >> j) & 1) {
                node = stjump[node][j];
                if (node == -1) {
                    return -1;
                }
            }
        }
        
        return node;
    }
};

/**
 * 测试用例
 */
int main() {
    // 测试用例1：简单的链式结构
    int n1 = 7;
    vector<int> parent1 = {-1, 0, 0, 1, 1, 2, 2};
    TreeAncestor ta1(n1, parent1);
    
    // 测试查询
    cout << "测试用例1:" << endl;
    cout << "节点3的第1个祖先: " << ta1.getKthAncestor(3, 1) << endl; // 期望: 1
    cout << "节点3的第2个祖先: " << ta1.getKthAncestor(3, 2) << endl; // 期望: 0
    cout << "节点3的第3个祖先: " << ta1.getKthAncestor(3, 3) << endl; // 期望: -1
    
    // 测试用例2：更复杂的树结构
    int n2 = 5;
    vector<int> parent2 = {-1, 0, 0, 1, 2};
    TreeAncestor ta2(n2, parent2);
    
    cout << "\\n测试用例2:" << endl;
    cout << "节点4的第1个祖先: " << ta2.getKthAncestor(4, 1) << endl; // 期望: 2
    cout << "节点4的第2个祖先: " << ta2.getKthAncestor(4, 2) << endl; // 期望: 0
    cout << "节点4的第3个祖先: " << ta2.getKthAncestor(4, 3) << endl; // 期望: -1
    
    return 0;
}