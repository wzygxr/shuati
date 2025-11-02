/**
 * 子树匹配算法及其应用题目集合 - C++版本
 * 
 * 本文件实现了子树匹配的核心算法，并提供了多个相关题目的C++解决方案
 * 子树匹配是二叉树操作中的经典问题，主要应用于树形结构的比较、搜索等场景
 * 
 * 核心思想：
 * 1. 暴力递归法：遍历每个节点，检查以该节点为根的子树是否与目标子树相同
 * 2. 序列化+KMP算法：将树序列化为字符串，使用KMP算法查找子序列
 * 
 * 应用场景：
 * - 树形结构相似度比较
 * - XML/JSON文档片段匹配
 * - 代码结构分析
 * - 模式识别中的树形结构匹配
 */

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <sstream>
#include <algorithm>

using namespace std;

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

// 链表节点定义（用于LeetCode 1367题）
struct ListNode {
    int val;
    ListNode *next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

/**
 * LeetCode 572: 另一棵树的子树
 * 暴力递归解法
 * 
 * 算法思路：
 * 1. 遍历树t1的每个节点
 * 2. 对于每个节点，检查以该节点为根的子树是否与t2相同
 * 3. 如果相同，返回true
 * 4. 如果遍历完所有节点都没有找到匹配的子树，返回false
 *
 * 时间复杂度：O(n * m)，其中n是t1的节点数，m是t2的节点数
 * 空间复杂度：O(max(n, m))，递归调用栈的深度
 *
 * @param t1 主树
 * @param t2 子树
 * @return 如果t1包含t2返回true，否则返回false
 */
bool sameTree(TreeNode* a, TreeNode* b) {
    if (a == nullptr && b == nullptr) {
        return true;
    }
    if (a != nullptr && b != nullptr) {
        return a->val == b->val && sameTree(a->left, b->left) && sameTree(a->right, b->right);
    }
    return false;
}

bool isSubtree(TreeNode* t1, TreeNode* t2) {
    if (t1 != nullptr && t2 != nullptr) {
        return sameTree(t1, t2) || isSubtree(t1->left, t2) || isSubtree(t1->right, t2);
    }
    return t2 == nullptr;
}

/**
 * KMP算法辅助函数：构建next数组
 * @param s 模式串
 * @return next数组
 */
vector<int> buildNext(const vector<string>& s) {
    int m = s.size();
    vector<int> next(m, 0);
    if (m == 0) return next;
    
    next[0] = -1;
    if (m == 1) return next;
    
    next[1] = 0;
    int i = 2, cn = 0;
    while (i < m) {
        if (s[i-1] == s[cn]) {
            next[i++] = ++cn;
        } else if (cn > 0) {
            cn = next[cn];
        } else {
            next[i++] = 0;
        }
    }
    return next;
}

/**
 * 二叉树先序序列化
 * @param head 树的根节点
 * @param path 序列化结果存储的向量
 */
void serialize(TreeNode* head, vector<string>& path) {
    if (head == nullptr) {
        path.push_back("null");
    } else {
        path.push_back(to_string(head->val));
        serialize(head->left, path);
        serialize(head->right, path);
    }
}

/**
 * KMP算法在序列中查找子序列
 * @param s1 文本串序列
 * @param s2 模式串序列
 * @return 匹配的起始位置，如果不存在返回-1
 */
int kmp(const vector<string>& s1, const vector<string>& s2) {
    int n = s1.size(), m = s2.size();
    if (m > n) return -1;
    if (m == 0) return 0;
    
    vector<int> next = buildNext(s2);
    int x = 0, y = 0;
    
    while (x < n && y < m) {
        if (s1[x] == s2[y]) {
            x++;
            y++;
        } else if (y == 0) {
            x++;
        } else {
            y = next[y];
        }
    }
    
    return y == m ? x - y : -1;
}

/**
 * LeetCode 572: 另一棵树的子树
 * 二叉树先序序列化 + KMP算法匹配解法
 * 
 * 算法思路：
 * 1. 将两棵树进行先序序列化
 * 2. 使用KMP算法在t1的序列化结果中查找t2的序列化结果
 * 3. 如果能找到，说明t1包含t2作为子树
 *
 * 时间复杂度：O(n + m)，其中n是t1的节点数，m是t2的节点数
 * 空间复杂度：O(n + m)，用于存储序列化结果
 *
 * @param t1 主树
 * @param t2 子树
 * @return 如果t1包含t2返回true，否则返回false
 */
bool isSubtree2(TreeNode* t1, TreeNode* t2) {
    if (t1 != nullptr && t2 != nullptr) {
        vector<string> s1, s2;
        serialize(t1, s1);
        serialize(t2, s2);
        return kmp(s1, s2) != -1;
    }
    return t2 == nullptr;
}

/**
 * LeetCode 652: 寻找重复的子树
 * 题目描述：给定一棵二叉树，返回所有重复的子树
 * 对于同一类的重复子树，你只需要返回其中任意一棵的根结点即可
 * 两棵树重复是指它们具有相同的结构以及相同的结点值
 * 
 * 算法思路：
 * 1. 使用后序遍历序列化每个子树
 * 2. 使用哈希表记录每个序列化结果出现的次数
 * 3. 当某个序列化结果出现次数为2时，将对应子树的根节点加入结果集
 * 
 * 时间复杂度：O(n²)，其中n是树的节点数，每个节点可能需要O(n)时间序列化
 * 空间复杂度：O(n²)，存储所有子树的序列化结果
 * 
 * @param root 二叉树的根节点
 * @return 重复子树的根节点列表
 */
string serializeSubtree(TreeNode* node, unordered_map<string, int>& countMap, vector<TreeNode*>& result) {
    if (node == nullptr) {
        return "#";
    }
    
    // 后序遍历序列化
    string left = serializeSubtree(node->left, countMap, result);
    string right = serializeSubtree(node->right, countMap, result);
    
    // 构建当前子树的序列化字符串
    stringstream ss;
    ss << node->val << "," << left << "," << right;
    string serial = ss.str();
    
    // 计数并收集结果
    countMap[serial]++;
    if (countMap[serial] == 2) {
        result.push_back(node);
    }
    
    return serial;
}

vector<TreeNode*> findDuplicateSubtrees(TreeNode* root) {
    vector<TreeNode*> result;
    unordered_map<string, int> countMap;
    serializeSubtree(root, countMap, result);
    return result;
}

/**
 * LeetCode 1367: 二叉树中的链表
 * 题目描述：给定一棵二叉树，判断它是否包含一个子树，其结构与给定的链表完全相同
 * 链表中的节点值应与二叉树中的对应节点值完全匹配
 * 
 * 算法思路：
 * 1. 遍历二叉树的每个节点
 * 2. 对于每个节点，尝试匹配链表
 * 3. 使用DFS递归匹配
 * 
 * 时间复杂度：O(n*m)，其中n是树的节点数，m是链表长度
 * 空间复杂度：O(max(h, m))，h是树的高度，m是链表长度
 * 
 * @param head 链表头节点
 * @param root 二叉树根节点
 * @return 是否存在匹配
 */
bool dfsMatch(ListNode* head, TreeNode* root) {
    if (head == nullptr) {
        return true; // 链表匹配完成
    }
    if (root == nullptr) {
        return false; // 树遍历完但链表未匹配完
    }
    if (head->val != root->val) {
        return false; // 当前节点值不匹配
    }
    
    // 递归匹配下一个节点
    return dfsMatch(head->next, root->left) || dfsMatch(head->next, root->right);
}

bool isSubPath(ListNode* head, TreeNode* root) {
    if (head == nullptr) {
        return true;
    }
    if (root == nullptr) {
        return false;
    }
    
    // 检查当前节点是否能开始匹配，或者在左子树、右子树中寻找匹配
    return dfsMatch(head, root) || isSubPath(head, root->left) || isSubPath(head, root->right);
}

/**
 * LeetCode 951: 翻转等价二叉树
 * 题目描述：判断两棵二叉树是否是翻转等价的
 * 翻转等价的定义是：通过交换任意节点的左右子树若干次，可以使两棵树变得完全相同
 * 
 * 算法思路：
 * 1. 如果两个节点都为空，返回true
 * 2. 如果一个为空另一个不为空，或节点值不同，返回false
 * 3. 递归判断：要么不翻转直接匹配左右子树，要么翻转后匹配
 * 
 * 时间复杂度：O(min(n, m))，其中n和m是两棵树的节点数
 * 空间复杂度：O(min(h1, h2))，h1和h2是两棵树的高度
 * 
 * @param root1 第一棵树的根节点
 * @param root2 第二棵树的根节点
 * @return 是否翻转等价
 */
bool flipEquiv(TreeNode* root1, TreeNode* root2) {
    if (root1 == nullptr && root2 == nullptr) {
        return true;
    }
    if (root1 == nullptr || root2 == nullptr || root1->val != root2->val) {
        return false;
    }
    
    // 不翻转的情况 或 翻转的情况
    return (flipEquiv(root1->left, root2->left) && flipEquiv(root1->right, root2->right)) || 
           (flipEquiv(root1->left, root2->right) && flipEquiv(root1->right, root2->left));
}

/**
 * 释放二叉树内存的辅助函数
 */
void deleteTree(TreeNode* root) {
    if (root) {
        deleteTree(root->left);
        deleteTree(root->right);
        delete root;
    }
}

/**
 * 释放链表内存的辅助函数
 */
void deleteList(ListNode* head) {
    while (head) {
        ListNode* temp = head;
        head = head->next;
        delete temp;
    }
}

/**
 * 测试LeetCode 572: 另一棵树的子树
 */
void testSubtreeOfAnotherTree() {
    cout << "========== 测试 LeetCode 572: 另一棵树的子树 ==========" << endl;
    
    // 测试用例1: t1包含t2
    TreeNode* t1_root1 = new TreeNode(3);
    t1_root1->left = new TreeNode(4);
    t1_root1->right = new TreeNode(5);
    t1_root1->left->left = new TreeNode(1);
    t1_root1->left->right = new TreeNode(2);

    TreeNode* t2_root1 = new TreeNode(4);
    t2_root1->left = new TreeNode(1);
    t2_root1->right = new TreeNode(2);

    bool result1_method1 = isSubtree(t1_root1, t2_root1);
    bool result1_method2 = isSubtree2(t1_root1, t2_root1);

    cout << "测试用例1: " << endl;
    cout << "方法1结果: " << (result1_method1 ? "true" : "false") << "，期望输出: true" << endl;
    cout << "方法2结果: " << (result1_method2 ? "true" : "false") << "，期望输出: true" << endl << endl;

    // 测试用例2: t1不包含t2
    TreeNode* t1_root2 = new TreeNode(3);
    t1_root2->left = new TreeNode(4);
    t1_root2->right = new TreeNode(5);
    t1_root2->left->left = new TreeNode(1);
    t1_root2->left->right = new TreeNode(2);
    t1_root2->left->right->left = new TreeNode(0);

    TreeNode* t2_root2 = new TreeNode(4);
    t2_root2->left = new TreeNode(1);
    t2_root2->right = new TreeNode(2);

    bool result2_method1 = isSubtree(t1_root2, t2_root2);
    bool result2_method2 = isSubtree2(t1_root2, t2_root2);

    cout << "测试用例2: " << endl;
    cout << "方法1结果: " << (result2_method1 ? "true" : "false") << "，期望输出: false" << endl;
    cout << "方法2结果: " << (result2_method2 ? "true" : "false") << "，期望输出: false" << endl << endl;

    // 清理内存
    deleteTree(t1_root1);
    deleteTree(t2_root1);
    deleteTree(t1_root2);
    deleteTree(t2_root2);
}

/**
 * 测试LeetCode 652: 寻找重复的子树
 */
void testFindDuplicateSubtrees() {
    cout << "========== 测试 LeetCode 652: 寻找重复的子树 ==========" << endl;
    
    // 构建测试用例
    //     1
    //    / \
    //   2   3
    //  /   / \
    // 4   2   4
    //    /
    //   4
    TreeNode* root = new TreeNode(1);
    root->left = new TreeNode(2);
    root->right = new TreeNode(3);
    root->left->left = new TreeNode(4);
    root->right->left = new TreeNode(2);
    root->right->right = new TreeNode(4);
    root->right->left->left = new TreeNode(4);
    
    vector<TreeNode*> result = findDuplicateSubtrees(root);
    cout << "重复子树数量: " << result.size() << "，期望输出: 2" << endl;
    cout << "重复子树根节点值: ";
    for (TreeNode* node : result) {
        cout << node->val << " "; // 期望输出: 2 4 或 4 2
    }
    cout << endl << endl;
    
    // 清理内存
    deleteTree(root);
}

/**
 * 测试LeetCode 1367: 二叉树中的链表
 */
void testIsSubPath() {
    cout << "========== 测试 LeetCode 1367: 二叉树中的链表 ==========" << endl;
    
    // 构建测试用例1: 匹配
    // 链表: 4->2->8
    // 二叉树:
    //      1
    //     / \
    //    4   4
    //     \   \
    //      2   2
    //       \   \
    //        8   6
    //             \
    //              8
    ListNode* head1 = new ListNode(4);
    head1->next = new ListNode(2);
    head1->next->next = new ListNode(8);
    
    TreeNode* root1 = new TreeNode(1);
    root1->left = new TreeNode(4);
    root1->right = new TreeNode(4);
    root1->left->right = new TreeNode(2);
    root1->right->right = new TreeNode(2);
    root1->left->right->right = new TreeNode(8);
    root1->right->right->right = new TreeNode(6);
    root1->right->right->right->right = new TreeNode(8);
    
    bool result1 = isSubPath(head1, root1);
    cout << "测试用例1结果: " << (result1 ? "true" : "false") << "，期望输出: true" << endl;
    
    // 测试用例2: 匹配
    // 链表: 1->4->2->6->8
    // 在二叉树中存在路径: 1(根)->4(右子树)->2(右子树)->6(右子树)->8(右子树)
    ListNode* head2 = new ListNode(1);
    head2->next = new ListNode(4);
    head2->next->next = new ListNode(2);
    head2->next->next->next = new ListNode(6);
    head2->next->next->next->next = new ListNode(8);
    
    bool result2 = isSubPath(head2, root1);
    cout << "测试用例2结果: " << (result2 ? "true" : "false") << "，期望输出: true" << endl;
    cout << endl;
}

/**
 * 测试LeetCode 951: 翻转等价二叉树
 */
void testFlipEquiv() {
    cout << "========== 测试 LeetCode 951: 翻转等价二叉树 ==========" << endl;
    
    // 测试用例1: 翻转等价
    // 树1:
    //      1
    //     / \
    //    2   3
    //   / \   \
    //  4   5   6
    //     / \
    //    7   8
    TreeNode* root1 = new TreeNode(1);
    root1->left = new TreeNode(2);
    root1->right = new TreeNode(3);
    root1->left->left = new TreeNode(4);
    root1->left->right = new TreeNode(5);
    root1->right->right = new TreeNode(6);
    root1->left->right->left = new TreeNode(7);
    root1->left->right->right = new TreeNode(8);
    
    // 树2 (翻转后等价):
    //      1
    //     / \
    //    3   2
    //   /   / \
    //  6   5   4
    //     / \
    //    8   7
    TreeNode* root2 = new TreeNode(1);
    root2->left = new TreeNode(3);
    root2->right = new TreeNode(2);
    root2->left->left = new TreeNode(6);
    root2->right->left = new TreeNode(5);
    root2->right->right = new TreeNode(4);
    root2->right->left->left = new TreeNode(8);
    root2->right->left->right = new TreeNode(7);
    
    bool result1 = flipEquiv(root1, root2);
    cout << "测试用例1结果: " << (result1 ? "true" : "false") << "，期望输出: true" << endl;
    
    // 测试用例2: 不等价
    TreeNode* root3 = new TreeNode(1);
    root3->left = new TreeNode(2);
    root3->left->left = new TreeNode(3);
    
    TreeNode* root4 = new TreeNode(1);
    root4->left = new TreeNode(3);
    root4->right = new TreeNode(2);
    
    bool result2 = flipEquiv(root3, root4);
    cout << "测试用例2结果: " << (result2 ? "true" : "false") << "，期望输出: false" << endl << endl;
    
    // 清理内存
    deleteTree(root1);
    deleteTree(root2);
    deleteTree(root3);
    deleteTree(root4);
}

/**
 * 主函数，运行所有测试
 */
int main() {
    // 运行所有测试用例
    testSubtreeOfAnotherTree();
    testFindDuplicateSubtrees();
    testIsSubPath();
    testFlipEquiv();
    
    return 0;
}