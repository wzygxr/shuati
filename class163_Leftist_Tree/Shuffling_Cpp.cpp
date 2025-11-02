/**
 * AtCoder ARC065F Shuffling（洗牌）
 * 题目链接：https://atcoder.jp/contests/arc065/tasks/arc065_d
 * 
 * 题目描述：
 * 给定一个字符串s，其中包含字符'o'和'x'。我们可以执行任意次数的操作，
 * 每次操作可以选择任意两个字符进行交换。我们的目标是通过最少的交换次数，
 * 使得任意长度为k的连续子串中至少包含一个'o'。
 * 
 * 解题思路：
 * 使用左偏树（Leftist Tree）来维护区间内'o'的位置，以高效地找到最优的交换策略。
 * 左偏树是一种可合并堆，在本题中用作最小堆来快速获取最接近目标位置的'o'。
 * 
 * 算法步骤：
 * 1. 收集所有'o'的位置
 * 2. 计算最少需要多少个'o'才能覆盖所有长度为k的窗口（根据鸽巢原理）
 * 3. 使用左偏树维护可用的'o'位置，贪心地为每个窗口分配最近的'o'
 * 
 * 时间复杂度：O(n log n)，其中n是字符串长度
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * - Java实现：Shuffling_Java.java
 * - Python实现：Shuffling_Python.py
 * - C++实现：Shuffling_Cpp.cpp
 */

// 左偏树节点结构体
struct LeftistTreeNode {
    int position; // 'o'在原字符串中的位置
    int value;    // 值（这里是位置值，用于最小堆比较）
    int dist;     // 距离（空路径长度）
    LeftistTreeNode* left;
    LeftistTreeNode* right;
    
    /**
     * 构造函数
     * @param pos 'o'在原字符串中的位置
     */
    LeftistTreeNode(int pos) 
        : position(pos), value(pos), dist(0), left(0), right(0) {}
};

/**
 * 合并两个左偏树（小根堆）
 * @param a 第一棵左偏树的根节点
 * @param b 第二棵左偏树的根节点
 * @return 合并后的左偏树根节点
 */
LeftistTreeNode* merge(LeftistTreeNode* a, LeftistTreeNode* b) {
    // 处理空树情况
    if (!a) return b;
    if (!b) return a;
    
    // 维护小根堆性质：确保a的根节点值小于等于b的根节点值
    if (a->value > b->value) {
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

/**
 * 获取堆顶元素（最小值）
 * @param root 左偏树根节点
 * @return 堆顶元素节点
 */
LeftistTreeNode* getMin(LeftistTreeNode* root) {
    return root;
}

/**
 * 删除堆顶元素
 * @param root 左偏树根节点
 * @return 删除堆顶元素后的新根节点
 */
LeftistTreeNode* deleteMin(LeftistTreeNode* root) {
    if (!root) return 0;
    // 合并左右子树作为新的根节点
    LeftistTreeNode* newRoot = merge(root->left, root->right);
    delete root;
    return newRoot;
}

/**
 * 清理左偏树（释放内存）
 * @param root 左偏树根节点
 */
void cleanup(LeftistTreeNode* root) {
    if (!root) return;
    cleanup(root->left);
    cleanup(root->right);
    delete root;
}

/**
 * 计算最小交换次数
 * @param s 输入字符串，由'o'和'x'组成
 * @param k 窗口大小
 * @return 最少交换次数，若无法满足条件则返回-1
 */
int minSwaps(char* s, int k) {
    // 计算字符串长度
    int n = 0;
    while (s[n] != '\0') n++;
    
    // 收集所有'o'的位置
    int oPositions[100000]; // 假设最大长度
    int m = 0;
    for (int i = 0; i < n; i++) {
        if (s[i] == 'o') {
            oPositions[m++] = i;
        }
    }
    
    // 特殊情况处理：如果没有'o'，则无法满足条件
    if (m == 0) {
        return -1;
    }
    
    // 计算最少需要多少个'o'才能覆盖所有长度为k的窗口
    // 根据鸽巢原理，至少需要ceil(n/k)个'o'
    int required = (n + k - 1) / k;
    
    // 如果现有'o'的数量不足，返回-1
    if (m < required) {
        return -1;
    }
    
    // 使用左偏树来维护当前可以覆盖的o的位置
    LeftistTreeNode* minHeap = 0;
    int swaps = 0;
    
    // 遍历每个可能的窗口位置，计算需要移动的'o'
    int currentPos = 0;
    for (int i = 0; i < required; i++) {
        // 理想情况下，第i个'o'应该放在位置i*k
        int targetPos = i * k;
        
        // 如果当前没有足够的'o'可用，从后面的'o'中选择最近的
        while (currentPos < m && oPositions[currentPos] <= targetPos + (k - 1)) {
            minHeap = merge(minHeap, new LeftistTreeNode(oPositions[currentPos]));
            currentPos++;
        }
        
        // 获取最小的可用'o'的位置
        LeftistTreeNode* minNode = getMin(minHeap);
        int actualPos = minNode->position;
        
        // 计算需要交换的次数（这里简化了计算，实际是移动的距离）
        // 使用条件表达式模拟abs函数
        int diff = actualPos - targetPos;
        swaps += (diff < 0) ? -diff : diff;
        
        // 从堆中删除已使用的'o'
        minHeap = deleteMin(minHeap);
    }
    
    // 清理剩余内存
    cleanup(minHeap);
    
    return swaps;
}