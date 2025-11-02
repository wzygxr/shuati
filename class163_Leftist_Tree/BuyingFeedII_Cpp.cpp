/**
 * USACO 2010 Jan Gold Buying Feed II（购买饲料II）
 * 题目链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=10
 * 
 * 题目描述：
 * 有N个商店，每个商店有一定数量的饲料和对应的价格。我们需要购买恰好D单位的饲料，
 * 且每次购买只能在一个商店购买一定数量的饲料。求最小化总花费。
 * 
 * 解题思路：
 * 使用左偏树来维护每个商店的价格和库存，每次选择价格最低的商店购买尽可能多的饲料。
 * 这是一个贪心算法的应用，通过左偏树实现优先队列来高效地获取当前价格最低的商店。
 * 
 * 算法步骤：
 * 1. 将所有商店按价格构建左偏树（小根堆）
 * 2. 每次从堆顶取出价格最低的商店
 * 3. 在该商店购买尽可能多的饲料（不超过需求量和库存量）
 * 4. 更新剩余需求量和商店库存
 * 5. 如果商店库存为0，则从堆中删除
 * 6. 重复步骤2-5直到满足需求
 * 
 * 时间复杂度：O(N log N + D log N)，但实际上由于每次购买尽可能多，所以复杂度更低
 * 空间复杂度：O(N)
 * 
 * 相关题目：
 * - Java实现：BuyingFeedII_Java.java
 * - Python实现：BuyingFeedII_Python.py
 * - C++实现：BuyingFeedII_Cpp.cpp
 */

// 商店结构体
struct Store {
    int price;    // 价格
    int quantity; // 数量
    
    /**
     * 构造函数
     * @param p 饲料价格
     * @param q 饲料数量
     */
    Store(int p, int q) : price(p), quantity(q) {}
};

// 左偏树节点结构体
struct LeftistTreeNode {
    Store* store;         // 商店信息
    int dist;             // 距离（空路径长度）
    LeftistTreeNode* left;
    LeftistTreeNode* right;
    
    /**
     * 构造函数
     * @param s 商店信息
     */
    LeftistTreeNode(Store* s) 
        : store(s), dist(0), left(0), right(0) {}
};

/**
 * 合并两个左偏树（小根堆，按价格排序）
 * @param a 第一棵左偏树的根节点
 * @param b 第二棵左偏树的根节点
 * @return 合并后的左偏树根节点
 */
LeftistTreeNode* merge(LeftistTreeNode* a, LeftistTreeNode* b) {
    // 处理空树情况
    if (!a) return b;
    if (!b) return a;
    
    // 维护小根堆性质：确保a的根节点价格小于等于b的根节点价格
    if (a->store->price > b->store->price) {
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
 * 获取堆顶元素（价格最低的商店）
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
 * 清理左偏树
 * @param root 左偏树根节点
 */
void cleanup(LeftistTreeNode* root) {
    if (!root) return;
    cleanup(root->left);
    cleanup(root->right);
    delete root;
}