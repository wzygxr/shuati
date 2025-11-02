#include <iostream>
#include <vector>
#include <stack>
#include <queue>
#include <map>
#include <algorithm>

/**
 * N叉树（N-ary Tree）实现
 * N叉树是一种树数据结构，其中每个节点可以有0个或多个子节点
 * 
 * 常见应用场景：
 * 1. 组织结构图
 * 2. 文件系统目录结构
 * 3. XML/HTML文档解析
 * 4. 计算机网络路由
 * 5. 游戏开发中的场景树
 * 
 * 相关算法题目：
 * - LeetCode 589. N叉树的前序遍历 https://leetcode.cn/problems/n-ary-tree-preorder-traversal/
 * - LeetCode 590. N叉树的后序遍历 https://leetcode.cn/problems/n-ary-tree-postorder-traversal/
 * - LeetCode 429. N叉树的层序遍历 https://leetcode.cn/problems/n-ary-tree-level-order-traversal/
 * - LeetCode 559. N叉树的最大深度 https://leetcode.cn/problems/maximum-depth-of-n-ary-tree/
 * - LeetCode 1490. 克隆N叉树 https://leetcode.cn/problems/clone-n-ary-tree/
 * - LintCode 1522. N叉树的直径 https://www.lintcode.com/problem/1522/
 * - HackerRank N-ary Tree Level Order Traversal https://www.hackerrank.com/challenges/tree-level-order-traversal/problem
 * - 洛谷 P5598 【XR-4】文本编辑器 https://www.luogu.com.cn/problem/P5598
 * - 牛客 NC144 多叉树的直径 https://www.nowcoder.com/practice/a77b4f3d84bf4a7891519ffee9376df3
 */

class NaryTreeNode {
public:
    int val;
    std::vector<NaryTreeNode*> children;

    /**
     * 构造函数
     * @param val 节点值
     */
    NaryTreeNode(int val) : val(val) {
    }

    /**
     * 添加子节点
     * @param child 子节点指针
     */
    void addChild(NaryTreeNode* child) {
        children.push_back(child);
    }
};

class NaryTree {
public:
    /**
     * 前序遍历：根节点 -> 子节点（从左到右）
     * @param root N叉树的根节点
     * @return 前序遍历的结果列表
     */
    std::vector<int> preorderTraversal(NaryTreeNode* root) {
        std::vector<int> result;
        preorderHelper(root, result);
        return result;
    }

private:
    void preorderHelper(NaryTreeNode* node, std::vector<int>& result) {
        if (!node) {
            return;
        }
        // 先访问根节点
        result.push_back(node->val);
        // 再递归访问所有子节点
        for (auto child : node->children) {
            preorderHelper(child, result);
        }
    }

public:
    /**
     * 前序遍历的非递归实现
     * @param root N叉树的根节点
     * @return 前序遍历的结果列表
     */
    std::vector<int> preorderTraversalIterative(NaryTreeNode* root) {
        std::vector<int> result;
        if (!root) {
            return result;
        }

        std::stack<NaryTreeNode*> stack;
        stack.push(root);

        while (!stack.empty()) {
            NaryTreeNode* node = stack.top();
            stack.pop();
            result.push_back(node->val);
            // 注意：这里需要逆序压入子节点，以保证出栈顺序是从左到右
            for (auto it = node->children.rbegin(); it != node->children.rend(); ++it) {
                stack.push(*it);
            }
        }

        return result;
    }

    /**
     * 后序遍历：子节点（从左到右）-> 根节点
     * @param root N叉树的根节点
     * @return 后序遍历的结果列表
     */
    std::vector<int> postorderTraversal(NaryTreeNode* root) {
        std::vector<int> result;
        postorderHelper(root, result);
        return result;
    }

private:
    void postorderHelper(NaryTreeNode* node, std::vector<int>& result) {
        if (!node) {
            return;
        }
        // 先递归访问所有子节点
        for (auto child : node->children) {
            postorderHelper(child, result);
        }
        // 再访问根节点
        result.push_back(node->val);
    }

public:
    /**
     * 层序遍历（广度优先遍历）
     * @param root N叉树的根节点
     * @return 层序遍历的结果列表，每个子列表代表一层
     */
    std::vector<std::vector<int>> levelOrderTraversal(NaryTreeNode* root) {
        std::vector<std::vector<int>> result;
        if (!root) {
            return result;
        }

        std::queue<NaryTreeNode*> queue;
        queue.push(root);

        while (!queue.empty()) {
            int levelSize = queue.size();
            std::vector<int> currentLevel;

            for (int i = 0; i < levelSize; ++i) {
                NaryTreeNode* node = queue.front();
                queue.pop();
                currentLevel.push_back(node->val);
                // 将所有子节点加入队列
                for (auto child : node->children) {
                    queue.push(child);
                }
            }

            result.push_back(currentLevel);
        }

        return result;
    }

    /**
     * 计算N叉树的最大深度
     * @param root N叉树的根节点
     * @return 最大深度
     */
    int maxDepth(NaryTreeNode* root) {
        if (!root) {
            return 0;
        }
        int maxChildDepth = 0;
        for (auto child : root->children) {
            maxChildDepth = std::max(maxChildDepth, maxDepth(child));
        }
        return maxChildDepth + 1;
    }

    /**
     * 计算N叉树的节点总数
     * @param root N叉树的根节点
     * @return 节点总数
     */
    int countNodes(NaryTreeNode* root) {
        if (!root) {
            return 0;
        }
        int count = 1; // 当前节点
        for (auto child : root->children) {
            count += countNodes(child);
        }
        return count;
    }

    /**
     * 克隆一棵N叉树
     * @param root 原N叉树的根节点
     * @return 克隆后的N叉树的根节点
     */
    NaryTreeNode* cloneTree(NaryTreeNode* root) {
        if (!root) {
            return nullptr;
        }
        
        NaryTreeNode* clonedRoot = new NaryTreeNode(root->val);
        for (auto child : root->children) {
            clonedRoot->addChild(cloneTree(child));
        }
        
        return clonedRoot;
    }

    /**
     * 查找值为target的节点
     * @param root N叉树的根节点
     * @param target 目标值
     * @return 找到的节点，如果不存在返回nullptr
     */
    NaryTreeNode* findNode(NaryTreeNode* root, int target) {
        if (!root) {
            return nullptr;
        }
        if (root->val == target) {
            return root;
        }
        for (auto child : root->children) {
            NaryTreeNode* found = findNode(child, target);
            if (found) {
                return found;
            }
        }
        return nullptr;
    }

    /**
     * 打印N叉树的结构
     * @param root N叉树的根节点
     */
    void printTree(NaryTreeNode* root) {
        if (!root) {
            std::cout << "Empty tree" << std::endl;
            return;
        }
        printTreeHelper(root, 0);
    }

private:
    void printTreeHelper(NaryTreeNode* node, int level) {
        if (!node) {
            return;
        }
        // 打印缩进
        for (int i = 0; i < level; ++i) {
            std::cout << "  ";
        }
        std::cout << node->val << std::endl;
        // 递归打印子节点
        for (auto child : node->children) {
            printTreeHelper(child, level + 1);
        }
    }

public:
    /**
     * 从父节点数组构建N叉树
     * @param parent 父节点数组，parent[i]表示节点i的父节点
     * @return 构建的N叉树的根节点
     */
    NaryTreeNode* buildTreeFromParentArray(const std::vector<int>& parent) {
        if (parent.empty()) {
            return nullptr;
        }

        std::map<int, NaryTreeNode*> nodes;
        NaryTreeNode* root = nullptr;

        for (int i = 0; i < parent.size(); ++i) {
            // 创建当前节点
            if (nodes.find(i) == nodes.end()) {
                nodes[i] = new NaryTreeNode(i);
            }
            NaryTreeNode* current = nodes[i];

            if (parent[i] == -1) {
                // 根节点
                root = current;
            } else {
                // 创建父节点（如果不存在）
                if (nodes.find(parent[i]) == nodes.end()) {
                    nodes[parent[i]] = new NaryTreeNode(parent[i]);
                }
                NaryTreeNode* parentNode = nodes[parent[i]];
                // 将当前节点添加为父节点的子节点
                parentNode->addChild(current);
            }
        }

        return root;
    }

    /**
     * 判断两棵N叉树是否相同
     * @param p 第一棵树的根节点
     * @param q 第二棵树的根节点
     * @return 如果两棵树相同返回true，否则返回false
     */
    bool isSameTree(NaryTreeNode* p, NaryTreeNode* q) {
        if (!p && !q) {
            return true;
        }
        if (!p || !q) {
            return false;
        }
        if (p->val != q->val) {
            return false;
        }
        if (p->children.size() != q->children.size()) {
            return false;
        }

        for (size_t i = 0; i < p->children.size(); ++i) {
            if (!isSameTree(p->children[i], q->children[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * 计算N叉树的直径（最长路径）
     * @param root N叉树的根节点
     * @return 树的直径
     */
    int diameter(NaryTreeNode* root) {
        int maxDiameter = 0;
        diameterHelper(root, maxDiameter);
        return maxDiameter;
    }

private:
    int diameterHelper(NaryTreeNode* node, int& maxDiameter) {
        if (!node) {
            return 0;
        }

        // 记录最大的两个高度
        int max1 = 0, max2 = 0;
        for (auto child : node->children) {
            int height = diameterHelper(child, maxDiameter) + 1;
            if (height > max1) {
                max2 = max1;
                max1 = height;
            } else if (height > max2) {
                max2 = height;
            }
        }

        // 更新最大直径
        maxDiameter = std::max(maxDiameter, max1 + max2);
        return max1;
    }

public:
    /**
     * 释放N叉树的内存
     * @param root N叉树的根节点
     */
    void freeTree(NaryTreeNode* root) {
        if (!root) {
            return;
        }
        for (auto child : root->children) {
            freeTree(child);
        }
        delete root;
    }
};

int main() {
    // 创建N叉树示例
    //       1
    //     / | \
    //    2  3  4
    //   / \   / \
    //  5   6 7   8
    
    NaryTreeNode* root = new NaryTreeNode(1);
    NaryTreeNode* node2 = new NaryTreeNode(2);
    NaryTreeNode* node3 = new NaryTreeNode(3);
    NaryTreeNode* node4 = new NaryTreeNode(4);
    NaryTreeNode* node5 = new NaryTreeNode(5);
    NaryTreeNode* node6 = new NaryTreeNode(6);
    NaryTreeNode* node7 = new NaryTreeNode(7);
    NaryTreeNode* node8 = new NaryTreeNode(8);

    root->addChild(node2);
    root->addChild(node3);
    root->addChild(node4);
    node2->addChild(node5);
    node2->addChild(node6);
    node4->addChild(node7);
    node4->addChild(node8);

    NaryTree tree;

    // 测试前序遍历
    std::cout << "前序遍历（递归）:" << std::endl;
    std::vector<int> preorder = tree.preorderTraversal(root);
    for (int val : preorder) {
        std::cout << val << " " << std::endl;
    }
    std::cout << std::endl;

    std::cout << "前序遍历（非递归）:" << std::endl;
    std::vector<int> preorderIter = tree.preorderTraversalIterative(root);
    for (int val : preorderIter) {
        std::cout << val << " " << std::endl;
    }
    std::cout << std::endl;

    // 测试后序遍历
    std::cout << "后序遍历:" << std::endl;
    std::vector<int> postorder = tree.postorderTraversal(root);
    for (int val : postorder) {
        std::cout << val << " " << std::endl;
    }
    std::cout << std::endl;

    // 测试层序遍历
    std::cout << "层序遍历:" << std::endl;
    std::vector<std::vector<int>> levelOrder = tree.levelOrderTraversal(root);
    for (const auto& level : levelOrder) {
        for (int val : level) {
            std::cout << val << " " << std::endl;
        }
        std::cout << std::endl;
    }

    // 测试最大深度
    std::cout << "最大深度: " << tree.maxDepth(root) << std::endl;

    // 测试节点总数
    std::cout << "节点总数: " << tree.countNodes(root) << std::endl;

    // 测试克隆树
    NaryTreeNode* cloned = tree.cloneTree(root);
    std::cout << "克隆树前序遍历:" << std::endl;
    std::vector<int> clonedPreorder = tree.preorderTraversal(cloned);
    for (int val : clonedPreorder) {
        std::cout << val << " " << std::endl;
    }
    std::cout << std::endl;

    // 测试查找节点
    NaryTreeNode* found = tree.findNode(root, 6);
    std::cout << "查找节点6: " << (found ? "找到" : "未找到") << std::endl;

    // 测试打印树
    std::cout << "树的结构:" << std::endl;
    tree.printTree(root);

    // 测试从父节点数组构建树
    std::vector<int> parentArray = {-1, 0, 0, 0, 1, 1, 3, 3};
    NaryTreeNode* builtTree = tree.buildTreeFromParentArray(parentArray);
    std::cout << "从父节点数组构建的树结构:" << std::endl;
    tree.printTree(builtTree);

    // 测试判断树是否相同
    bool isSame = tree.isSameTree(root, cloned);
    std::cout << "原树与克隆树是否相同: " << (isSame ? "true" : "false") << std::endl;

    // 测试树的直径
    int diameter = tree.diameter(root);
    std::cout << "树的直径: " << diameter << std::endl;

    // 释放内存
    tree.freeTree(root);
    tree.freeTree(cloned);
    tree.freeTree(builtTree);

    return 0;
}