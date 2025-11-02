// LintCode 474. Lowest Common Ancestor II
// 题目链接: https://www.lintcode.com/problem/474/
// 给定一个二叉树中的节点 a 和 b，找出他们的最近公共祖先
// 每个节点包含一个指向其父节点的指针
// 
// 解题思路:
// 1. 由于每个节点都有父节点指针，可以将问题转化为链表相交问题
// 2. 从节点a开始，沿着父节点指针向上遍历，记录所有访问过的节点
// 3. 从节点b开始，沿着父节点指针向上遍历，第一个已经在步骤2中访问过的节点就是LCA
//
// 时间复杂度: O(h) - h为树的高度
// 空间复杂度: O(h) - 需要存储从a到根节点的路径
// 是否为最优解: 是，利用父节点指针可以避免遍历整棵树

// 不提交这个类
struct ParentTreeNode {
    int val;
    ParentTreeNode *parent, *left, *right;
};

// 提交如下的方法
ParentTreeNode * lowestCommonAncestorII(ParentTreeNode * root, ParentTreeNode * A, ParentTreeNode * B) {
    // 使用数组记录从节点A到根节点的路径（假设最多1000个节点）
    ParentTreeNode* path[1000];
    int pathLen = 0;
    
    // 从节点A开始向上遍历到根节点，记录路径上的所有节点
    ParentTreeNode* current = A;
    while (current != 0) {  // 用0代替NULL
        path[pathLen++] = current;
        current = current->parent;
    }
    
    // 从节点B开始向上遍历，第一个在path数组中的节点就是LCA
    current = B;
    while (current != 0) {  // 用0代替NULL
        // 检查current是否在path数组中
        for (int i = 0; i < pathLen; i++) {
            if (path[i] == current) {
                return current; // 找到LCA
            }
        }
        current = current->parent;
    }
    
    // 理论上不会执行到这里，因为A和B都在树中，肯定有公共祖先
    return 0;  // 用0代替NULL
}

// 测试代码示例（不提交）
/*
int main() {
    // 构造测试用例:
    //       4
    //      / \
    //     3   7
    //        / \
    //       5   6
    ParentTreeNode* node4 = new ParentTreeNode();
    ParentTreeNode* node3 = new ParentTreeNode();
    ParentTreeNode* node7 = new ParentTreeNode();
    ParentTreeNode* node5 = new ParentTreeNode();
    ParentTreeNode* node6 = new ParentTreeNode();
    
    node4->val = 4;
    node3->val = 3;
    node7->val = 7;
    node5->val = 5;
    node6->val = 6;

    // 设置父子关系
    node3->parent = node4;
    node7->parent = node4;
    node5->parent = node7;
    node6->parent = node7;

    node4->left = node3;
    node4->right = node7;
    node7->left = node5;
    node7->right = node6;
    
    // 测试LCA(3, 5) = 4
    ParentTreeNode* result1 = lowestCommonAncestorII(node4, node3, node5);
    // printf("LCA(3, 5) = %d\n", result1->val); // 应该输出4

    // 测试LCA(5, 6) = 7
    ParentTreeNode* result2 = lowestCommonAncestorII(node4, node5, node6);
    // printf("LCA(5, 6) = %d\n", result2->val); // 应该输出7

    // 测试LCA(6, 7) = 7
    ParentTreeNode* result3 = lowestCommonAncestorII(node4, node6, node7);
    // printf("LCA(6, 7) = %d\n", result3->val); // 应该输出7

    return 0;
}
*/