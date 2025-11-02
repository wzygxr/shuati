/**
 * 牛客 NC20828 [HNOI2004] 树的同构
 * 题目链接：https://ac.nowcoder.com/acm/problem/20828
 * 
 * 题目描述：
 * 给定两棵有根树，判断它们是否同构。同构的定义是：两棵树可以通过若干次交换子节点的顺序得到彼此。
 * 
 * 解题思路：
 * 使用左偏树维护哈希值，进行树的同构判断。
 * 通过递归计算每棵树的哈希值，同构的树会得到相同的哈希值。
 * 
 * 算法步骤：
 * 1. 对于每棵树，递归计算每个节点的哈希值
 * 2. 使用左偏树对子节点的哈希值进行排序
 * 3. 将排序后的子节点哈希值合并成当前节点的哈希值
 * 4. 比较所有树的哈希值，相同哈希值的树是同构的
 * 
 * 时间复杂度：O(N log N)，其中N是节点总数
 * 空间复杂度：O(N)
 * 
 * 相关题目：
 * - Java实现：TreeIsomorphism_Java.java
 * - Python实现：TreeIsomorphism_Python.py
 * - C++实现：TreeIsomorphism_Cpp.cpp
 */

// 左偏树节点类
struct LeftistTreeNode {
    long long hash;  // 哈希值
    int dist;        // 距离（空路径长度）
    LeftistTreeNode *left, *right;
    
    /**
     * 构造函数
     * @param h 哈希值
     */
    LeftistTreeNode(long long h) : hash(h), dist(0), left(0), right(0) {}
};

/**
 * 合并两个左偏树
 * @param a 第一棵左偏树的根节点
 * @param b 第二棵左偏树的根节点
 * @return 合并后的左偏树根节点
 */
LeftistTreeNode* merge(LeftistTreeNode* a, LeftistTreeNode* b) {
    // 处理空树情况
    if (!a) return b;
    if (!b) return a;
    
    // 维护大根堆性质：确保a的根节点哈希值大于等于b的根节点哈希值
    if (a->hash < b->hash) {
        LeftistTreeNode* temp = a;
        a = b;
        b = temp;
    }
    
    // 递归合并a的右子树与b
    a->right = merge(a->right, b);
    
    // 维护左偏性质：左子树的距离应大于等于右子树的距离
    if (!a->left || (a->right && a->left->dist < a->right->dist)) {
        LeftistTreeNode* temp = a->left;
        a->left = a->right;
        a->right = temp;
    }
    
    // 更新距离：叶子节点距离为0，非叶子节点距离为其右子树距离+1
    a->dist = a->right ? a->right->dist + 1 : 0;
    return a;
}

// 树节点类
struct TreeNode {
    int id;
    TreeNode* children[1000];  // 假设最多1000个子节点
    int childCount;
    
    /**
     * 构造函数
     * @param i 节点ID
     */
    TreeNode(int i) : id(i), childCount(0) {}
};

/**
 * 计算树的哈希值
 * @param root 树的根节点
 * @return 树的哈希值
 */
long long computeHash(TreeNode* root) {
    // 空节点的哈希值为0
    if (!root) return 0;
    
    // 使用左偏树维护子节点的哈希值
    LeftistTreeNode* heap = 0;
    for (int i = 0; i < root->childCount; i++) {
        long long childHash = computeHash(root->children[i]);
        heap = merge(heap, new LeftistTreeNode(childHash));
    }
    
    // 计算当前节点的哈希值，结合子节点的哈希值
    long long hash = 1; // 初始哈希值
    
    // 收集所有子节点的哈希值
    long long childHashes[1000];
    int hashCount = 0;
    while (heap) {
        childHashes[hashCount++] = heap->hash;
        // 保存左右子树
        LeftistTreeNode* left = heap->left;
        LeftistTreeNode* right = heap->right;
        delete heap; // 释放当前节点
        heap = merge(left, right);
    }
    
    // 对子节点的哈希值排序，确保同构的树得到相同的哈希值
    // 简单的冒泡排序
    for (int i = 0; i < hashCount - 1; i++) {
        for (int j = 0; j < hashCount - 1 - i; j++) {
            if (childHashes[j] > childHashes[j + 1]) {
                long long temp = childHashes[j];
                childHashes[j] = childHashes[j + 1];
                childHashes[j + 1] = temp;
            }
        }
    }
    
    for (int i = 0; i < hashCount; i++) {
        hash = hash * 1000003 + childHashes[i]; // 使用大质数作为基数
    }
    
    return hash;
}