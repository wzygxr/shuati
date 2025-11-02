/*
 * LeetCode 1367. 二叉树中的链表 - LinkedList in Binary Tree
 * 
 * 题目来源：LeetCode (力扣)
 * 题目链接：https://leetcode.cn/problems/linked-list-in-binary-tree/
 * 
 * 题目描述：
 * 给你一棵以root为根的二叉树和一个head为第一个节点的链表。
 * 如果在二叉树中，存在一条一直向下的路径，且每个点的数值恰好一一对应以head为首的链表中每个节点的值，那么请你返回True，否则返回False。
 * 一直向下的路径的意思是：从树中某个节点开始，一直连续向下的路径。
 * 
 * 算法思路：
 * 使用KMP算法结合二叉树遍历来解决这个问题。
 * 1. 将链表转换为数组
 * 2. 使用KMP算法预处理模式串（链表值序列）
 * 3. 在二叉树遍历过程中使用KMP状态机进行匹配
 * 4. 当匹配到完整链表时返回true
 * 
 * 时间复杂度：O(n + m)，其中n是二叉树节点数，m是链表长度
 * 空间复杂度：O(m)，用于存储next数组和链表数组
 * 
 * 工程化考量：
 * 1. 使用递归和迭代两种方式实现二叉树遍历
 * 2. 边界条件处理：空树、空链表等
 * 3. 异常处理确保程序稳定性
 * 4. 支持大规模数据输入
 */

#include <iostream>
#include <vector>
#include <stack>
#include <queue>
#include <cassert>
using namespace std;

// 链表节点定义
struct ListNode {
    int val;
    ListNode *next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

/**
 * 构建KMP算法的next数组（部分匹配表）
 * next[i]表示pattern[0...i]子串的最长相等前后缀的长度
 * 
 * @param pattern 模式数组（链表值序列）
 * @return next数组
 */
vector<int> buildNextArray(const vector<int>& pattern) {
    int m = pattern.size();
    if (m == 0) return vector<int>();
    
    vector<int> next(m, 0);
    next[0] = 0; // 第一个元素的next值为0
    
    int prefixLen = 0; // 当前最长相等前后缀的长度
    int i = 1;         // 当前处理的位置
    
    while (i < m) {
        // 当前值匹配，可以延长相等前后缀
        if (pattern[i] == pattern[prefixLen]) {
            prefixLen++;
            next[i] = prefixLen;
            i++;
        } 
        // 当前值不匹配，但prefixLen > 0，需要回退
        else if (prefixLen > 0) {
            prefixLen = next[prefixLen - 1];
        } 
        // 当前值不匹配且prefixLen = 0，next[i] = 0
        else {
            next[i] = 0;
            i++;
        }
    }
    
    return next;
}

/**
 * 深度优先搜索遍历二叉树
 * 在遍历过程中使用KMP状态机进行匹配
 * 
 * @param node 当前二叉树节点
 * @param pattern 链表值模式数组
 * @param next KMP next数组
 * @param state 当前KMP匹配状态
 * @return 是否存在匹配路径
 */
bool dfs(TreeNode* node, const vector<int>& pattern, const vector<int>& next, int state) {
    // 如果当前节点为空，返回false
    if (node == nullptr) return false;
    
    // KMP匹配过程
    while (state > 0 && node->val != pattern[state]) {
        state = next[state - 1];
    }
    
    if (node->val == pattern[state]) {
        state++;
    }
    
    // 如果完全匹配到链表，返回true
    if (state == pattern.size()) {
        return true;
    }
    
    // 递归遍历左右子树
    return dfs(node->left, pattern, next, state) || 
           dfs(node->right, pattern, next, state);
}

/**
 * 判断二叉树中是否存在与链表匹配的路径
 * 使用KMP算法优化匹配过程
 * 
 * @param head 链表头节点
 * @param root 二叉树根节点
 * @return 是否存在匹配路径
 */
bool isSubPath(ListNode* head, TreeNode* root) {
    // 边界条件处理
    if (head == nullptr) return true;  // 空链表总是匹配
    if (root == nullptr) return false; // 空树无法匹配非空链表
    
    // 将链表转换为数组
    vector<int> pattern;
    ListNode* current = head;
    while (current != nullptr) {
        pattern.push_back(current->val);
        current = current->next;
    }
    
    // 构建KMP算法的next数组
    vector<int> next = buildNextArray(pattern);
    
    // 使用DFS遍历二叉树并进行KMP匹配
    return dfs(root, pattern, next, 0);
}

/**
 * 迭代方式实现 - 使用栈进行DFS遍历
 * 避免递归深度过大导致的栈溢出
 * 
 * @param head 链表头节点
 * @param root 二叉树根节点
 * @return 是否存在匹配路径
 */
bool isSubPathIterative(ListNode* head, TreeNode* root) {
    if (head == nullptr) return true;
    if (root == nullptr) return false;
    
    // 将链表转换为数组
    vector<int> pattern;
    ListNode* current = head;
    while (current != nullptr) {
        pattern.push_back(current->val);
        current = current->next;
    }
    
    // 构建KMP算法的next数组
    vector<int> next = buildNextArray(pattern);
    
    // 使用栈进行DFS遍历
    stack<pair<TreeNode*, int>> stk; // 存储节点和当前匹配状态
    stk.push({root, 0});
    
    while (!stk.empty()) {
        auto [node, state] = stk.top();
        stk.pop();
        
        // KMP匹配过程
        int currentState = state;
        while (currentState > 0 && node->val != pattern[currentState]) {
            currentState = next[currentState - 1];
        }
        
        if (node->val == pattern[currentState]) {
            currentState++;
        }
        
        // 如果完全匹配到链表，返回true
        if (currentState == pattern.size()) {
            return true;
        }
        
        // 将左右子节点压入栈（先右后左，保证左子树先处理）
        if (node->right != nullptr) {
            stk.push({node->right, currentState});
        }
        if (node->left != nullptr) {
            stk.push({node->left, currentState});
        }
    }
    
    return false;
}

/**
 * 验证结果的辅助方法
 * 创建测试用例并验证算法正确性
 */
void verifyResults() {
    cout << "=== 验证测试开始 ===" << endl;
    
    // 测试用例1：简单匹配
    ListNode* head1 = new ListNode(1, new ListNode(2));
    TreeNode* root1 = new TreeNode(1, 
        new TreeNode(2), 
        new TreeNode(3)
    );
    bool result1 = isSubPath(head1, root1);
    cout << "测试用例1 - 简单匹配: " << result1 << endl;
    assert(result1 == true && "测试用例1验证失败");
    
    // 测试用例2：不匹配
    ListNode* head2 = new ListNode(1, new ListNode(4));
    TreeNode* root2 = new TreeNode(1, 
        new TreeNode(2), 
        new TreeNode(3)
    );
    bool result2 = isSubPath(head2, root2);
    cout << "测试用例2 - 不匹配: " << result2 << endl;
    assert(result2 == false && "测试用例2验证失败");
    
    // 测试用例3：多层匹配
    ListNode* head3 = new ListNode(1, new ListNode(2, new ListNode(3)));
    TreeNode* root3 = new TreeNode(1, 
        new TreeNode(2, 
            new TreeNode(3), 
            nullptr
        ), 
        new TreeNode(4)
    );
    bool result3 = isSubPath(head3, root3);
    cout << "测试用例3 - 多层匹配: " << result3 << endl;
    assert(result3 == true && "测试用例3验证失败");
    
    // 测试用例4：边界情况 - 空链表
    ListNode* head4 = nullptr;
    TreeNode* root4 = new TreeNode(1);
    bool result4 = isSubPath(head4, root4);
    cout << "测试用例4 - 空链表: " << result4 << endl;
    assert(result4 == true && "测试用例4验证失败");
    
    // 测试用例5：边界情况 - 空树
    ListNode* head5 = new ListNode(1);
    TreeNode* root5 = nullptr;
    bool result5 = isSubPath(head5, root5);
    cout << "测试用例5 - 空树: " << result5 << endl;
    assert(result5 == false && "测试用例5验证失败");
    
    cout << "=== 所有测试用例验证通过 ===" << endl;
    
    // 清理内存
    delete head1; delete root1;
    delete head2; delete root2;
    delete head3; delete root3;
    delete head4; delete root4;
    delete head5; // root5为nullptr，无需删除
}

/**
 * 创建大规模二叉树用于测试
 */
TreeNode* createLargeTree(int nodeCount) {
    if (nodeCount <= 0) return nullptr;
    
    TreeNode* root = new TreeNode(1);
    queue<TreeNode*> q;
    q.push(root);
    int count = 1;
    
    while (count < nodeCount && !q.empty()) {
        TreeNode* node = q.front();
        q.pop();
        
        if (count < nodeCount) {
            node->left = new TreeNode(++count);
            q.push(node->left);
        }
        
        if (count < nodeCount) {
            node->right = new TreeNode(++count);
            q.push(node->right);
        }
    }
    
    return root;
}

/**
 * 创建长链表用于测试
 */
ListNode* createLongList(int length) {
    if (length <= 0) return nullptr;
    
    ListNode* head = new ListNode(1);
    ListNode* current = head;
    
    for (int i = 2; i <= length; i++) {
        current->next = new ListNode(i);
        current = current->next;
    }
    
    return head;
}

/**
 * 性能测试方法
 * 测试大规模数据的处理能力
 */
void performanceTest() {
    cout << "\n=== 性能测试开始 ===" << endl;
    
    // 创建大规模测试数据
    int nodeCount = 10000;
    TreeNode* largeTree = createLargeTree(nodeCount);
    ListNode* longList = createLongList(1000);
    
    auto start = chrono::high_resolution_clock::now();
    
    bool result = isSubPath(longList, largeTree);
    
    auto end = chrono::high_resolution_clock::now();
    chrono::duration<double> duration = end - start;
    
    cout << "性能测试 - 二叉树节点数: " << nodeCount 
         << ", 链表长度: 1000" << endl;
    cout << "匹配结果: " << result << endl;
    cout << "执行时间: " << duration.count() * 1000 << " 毫秒" << endl;
    
    // 清理内存
    delete largeTree;
    delete longList;
    
    cout << "=== 性能测试完成 ===" << endl;
}

/**
 * 演示用例方法
 */
void demo() {
    cout << "\n=== 演示用例 ===" << endl;
    
    ListNode* demoHead = new ListNode(1, new ListNode(2, new ListNode(3)));
    TreeNode* demoRoot = new TreeNode(1, 
        new TreeNode(2, 
            new TreeNode(3), 
            new TreeNode(4)
        ), 
        new TreeNode(5)
    );
    
    bool demoResult = isSubPath(demoHead, demoRoot);
    cout << "演示用例匹配结果: " << demoResult << endl;
    
    // 清理内存
    delete demoHead;
    delete demoRoot;
}

int main() {
    // 运行验证测试
    verifyResults();
    
    // 运行性能测试
    performanceTest();
    
    // 运行演示用例
    demo();
    
    return 0;
}