// C++实现森林（Forest）数据结构
// 森林是由多棵不相交的树组成的数据结构

/**
 * 常见应用场景：
 * 1. 多棵独立树的集合管理
 * 2. 并查集（Union-Find）的基础
 * 3. 数据库中的多树索引
 * 4. 社交网络中的多个独立社区
 * 5. 并行计算中的任务调度树
 *
 * 相关算法题目：
 * - LeetCode 684. 冗余连接 https://leetcode.cn/problems/redundant-connection/
 * - LeetCode 685. 冗余连接 II https://leetcode.cn/problems/redundant-connection-ii/
 * - LeetCode 1258. 近义词句子 https://leetcode.cn/problems/synonymous-sentences/
 * - LeetCode 959. 由斜杠划分区域 https://leetcode.cn/problems/regions-cut-by-slashes/
 * - LintCode 1179. 连通分量 https://www.lintcode.com/problem/1179/
 * - 洛谷 P3367 【模板】并查集 https://www.luogu.com.cn/problem/P3367
 * - 牛客 NC233 合并二叉树 https://www.nowcoder.com/practice/a5e8156e81224147bd749c560909299a
 * - HackerRank Tree: Level Order Traversal https://www.hackerrank.com/challenges/tree-level-order-traversal/problem
 * - CodeChef FORESTGA https://www.codechef.com/problems/FORESTGA
 * - USACO Forest Fires https://usaco.org/index.php?page=viewproblem2&cpid=668
 */

#include <iostream>
#include <vector>
#include <queue>
#include <unordered_map>
#include <unordered_set>
#include <memory>
#include <string>
#include <algorithm>

// 树节点类
class TreeNode {
public:
    int val;
    std::vector<std::shared_ptr<TreeNode>> children;

    /**
     * 构造函数
     * @param val 节点值
     */
    explicit TreeNode(int val) : val(val) {}

    /**
     * 添加子节点
     * @param child 子节点
     */
    void addChild(const std::shared_ptr<TreeNode>& child) {
        children.push_back(child);
    }
};

// 森林类
class Forest {
private:
    std::vector<std::shared_ptr<TreeNode>> trees; // 森林中的所有树

    /**
     * 计算单棵树的节点数量（递归辅助函数）
     * @param root 树的根节点
     * @return 节点数量
     */
    int countNodes(const std::shared_ptr<TreeNode>& root) const {
        if (!root) {
            return 0;
        }
        int count = 1; // 当前节点
        for (const auto& child : root->children) {
            count += countNodes(child);
        }
        return count;
    }

    /**
     * 计算单棵树的高度（递归辅助函数）
     * @param root 树的根节点
     * @return 树的高度
     */
    int getTreeHeight(const std::shared_ptr<TreeNode>& root) const {
        if (!root) {
            return 0;
        }
        int maxChildHeight = 0;
        for (const auto& child : root->children) {
            maxChildHeight = std::max(maxChildHeight, getTreeHeight(child));
        }
        return maxChildHeight + 1;
    }

    /**
     * 在单棵树中查找节点（递归辅助函数）
     * @param root 树的根节点
     * @param target 目标值
     * @return 找到的节点，如果不存在返回nullptr
     */
    std::shared_ptr<TreeNode> findNodeInTree(const std::shared_ptr<TreeNode>& root, int target) const {
        if (!root) {
            return nullptr;
        }
        if (root->val == target) {
            return root;
        }
        for (const auto& child : root->children) {
            auto found = findNodeInTree(child, target);
            if (found) {
                return found;
            }
        }
        return nullptr;
    }

    /**
     * 单棵树的层序遍历（辅助函数）
     * @param root 树的根节点
     * @return 层序遍历的结果列表
     */
    std::vector<std::vector<int>> levelOrderTraversalTree(const std::shared_ptr<TreeNode>& root) const {
        std::vector<std::vector<int>> result;
        if (!root) {
            return result;
        }

        std::queue<std::shared_ptr<TreeNode>> queue;
        queue.push(root);

        while (!queue.empty()) {
            int levelSize = queue.size();
            std::vector<int> currentLevel;

            for (int i = 0; i < levelSize; ++i) {
                auto node = queue.front();
                queue.pop();
                currentLevel.push_back(node->val);
                for (const auto& child : node->children) {
                    queue.push(child);
                }
            }

            result.push_back(currentLevel);
        }

        return result;
    }

    /**
     * 打印单棵树的结构（辅助函数）
     * @param node 当前节点
     * @param level 当前节点的层级
     */
    void printTree(const std::shared_ptr<TreeNode>& node, int level) const {
        if (!node) {
            return;
        }
        // 打印缩进
        for (int i = 0; i < level; ++i) {
            std::cout << "  ";
        }
        std::cout << node->val << std::endl;
        // 递归打印子节点
        for (const auto& child : node->children) {
            printTree(child, level + 1);
        }
    }

    /**
     * 构建并查集（递归辅助函数）
     * @param node 当前节点
     * @param parent 父节点值
     * @param parentMap 父节点映射
     */
    void buildUnionFind(const std::shared_ptr<TreeNode>& node, int parent, 
                       std::unordered_map<int, int>& parentMap) const {
        if (!node) {
            return;
        }
        parentMap[node->val] = parent;
        for (const auto& child : node->children) {
            buildUnionFind(child, node->val, parentMap);
        }
    }

    /**
     * 收集树中的所有节点（辅助函数）
     * @param node 当前节点
     * @param component 节点集合
     */
    void collectNodes(const std::shared_ptr<TreeNode>& node, 
                     std::unordered_set<int>& component) const {
        if (!node) {
            return;
        }
        component.insert(node->val);
        for (const auto& child : node->children) {
            collectNodes(child, component);
        }
    }

    /**
     * 查找节点所在树的根节点值
     * @param nodeVal 节点值
     * @return 根节点值，如果节点不存在返回-2
     */
    int findRoot(int nodeVal) const {
        auto unionFind = toUnionFind();
        
        if (unionFind.find(nodeVal) == unionFind.end()) {
            return -2; // 节点不存在
        }
        
        // 查找根节点
        int current = nodeVal;
        while (unionFind[current] != -1) {
            current = unionFind[current];
        }
        
        return current;
    }

    /**
     * 深拷贝一棵树
     * @param root 树的根节点
     * @return 拷贝后的树的根节点
     */
    std::shared_ptr<TreeNode> cloneTree(const std::shared_ptr<TreeNode>& root) const {
        if (!root) {
            return nullptr;
        }
        auto newRoot = std::make_shared<TreeNode>(root->val);
        for (const auto& child : root->children) {
            newRoot->addChild(cloneTree(child));
        }
        return newRoot;
    }

    /**
     * 释放树的内存（非智能指针版本需要）
     * @param root 树的根节点
     */
    void freeTree(TreeNode* root) {
        if (!root) {
            return;
        }
        for (auto& child : root->children) {
            freeTree(child.get());
        }
        // 智能指针自动管理内存，不需要手动delete
    }

public:
    /**
     * 构造函数
     */
    Forest() = default;

    /**
     * 析构函数
     */
    ~Forest() = default;

    /**
     * 添加一棵树到森林
     * @param root 树的根节点
     */
    void addTree(const std::shared_ptr<TreeNode>& root) {
        if (root) {
            trees.push_back(root);
        }
    }

    /**
     * 计算森林中树的数量
     * @return 树的数量
     */
    int getTreeCount() const {
        return trees.size();
    }

    /**
     * 获取森林中的所有树
     * @return 树的列表
     */
    std::vector<std::shared_ptr<TreeNode>> getTrees() const {
        return trees;
    }

    /**
     * 计算森林中节点的总数
     * @return 节点总数
     */
    int getTotalNodeCount() const {
        int count = 0;
        for (const auto& root : trees) {
            count += countNodes(root);
        }
        return count;
    }

    /**
     * 计算森林中所有树的高度之和
     * @return 高度之和
     */
    int getTotalHeight() const {
        int totalHeight = 0;
        for (const auto& root : trees) {
            totalHeight += getTreeHeight(root);
        }
        return totalHeight;
    }

    /**
     * 在森林中查找值为target的节点
     * @param target 目标值
     * @return 找到的节点，如果不存在返回nullptr
     */
    std::shared_ptr<TreeNode> findNode(int target) const {
        for (const auto& root : trees) {
            auto found = findNodeInTree(root, target);
            if (found) {
                return found;
            }
        }
        return nullptr;
    }

    /**
     * 合并两棵树（将tree2合并到tree1）
     * @param tree1 第一棵树的根节点
     * @param tree2 第二棵树的根节点
     * @return 合并后的树的根节点
     */
    std::shared_ptr<TreeNode> mergeTrees(const std::shared_ptr<TreeNode>& tree1, 
                                       const std::shared_ptr<TreeNode>& tree2) {
        if (!tree1) {
            return tree2;
        }
        if (!tree2) {
            return tree1;
        }

        // 将tree2作为tree1的一个子节点
        tree1->addChild(tree2);
        
        // 从森林中移除tree2
        auto it = std::find(trees.begin(), trees.end(), tree2);
        if (it != trees.end()) {
            trees.erase(it);
        }
        
        return tree1;
    }

    /**
     * 将树从森林中移除
     * @param root 要移除的树的根节点
     * @return 如果移除成功返回true，否则返回false
     */
    bool removeTree(const std::shared_ptr<TreeNode>& root) {
        auto it = std::find(trees.begin(), trees.end(), root);
        if (it != trees.end()) {
            trees.erase(it);
            return true;
        }
        return false;
    }

    /**
     * 森林的层序遍历
     * @return 层序遍历的结果列表
     */
    std::vector<std::vector<int>> levelOrderTraversal() const {
        std::vector<std::vector<int>> result;
        for (const auto& root : trees) {
            auto treeLevelOrder = levelOrderTraversalTree(root);
            result.insert(result.end(), treeLevelOrder.begin(), treeLevelOrder.end());
        }
        return result;
    }

    /**
     * 打印森林的结构
     */
    void printForest() const {
        std::cout << "森林包含 " << trees.size() << " 棵树：" << std::endl;
        for (int i = 0; i < trees.size(); ++i) {
            std::cout << "\n树 " << (i + 1) << ":" << std::endl;
            printTree(trees[i], 0);
        }
    }

    /**
     * 将森林转换为并查集（基于父指针）
     * @return 并查集映射，其中key是节点值，value是父节点值
     */
    std::unordered_map<int, int> toUnionFind() const {
        std::unordered_map<int, int> parentMap;
        for (const auto& root : trees) {
            buildUnionFind(root, -1, parentMap);
        }
        return parentMap;
    }

    /**
     * 从并查集构建森林
     * @param parentMap 并查集父节点映射
     * @return 构建的森林
     */
    static Forest fromUnionFind(const std::unordered_map<int, int>& parentMap) {
        Forest forest;
        std::unordered_map<int, std::shared_ptr<TreeNode>> nodeMap;
        std::vector<int> roots;

        // 创建所有节点并找出根节点
        for (const auto& [nodeVal, parentVal] : parentMap) {
            // 创建节点（如果不存在）
            if (nodeMap.find(nodeVal) == nodeMap.end()) {
                nodeMap[nodeVal] = std::make_shared<TreeNode>(nodeVal);
            }
            
            // 如果是根节点（父节点为-1）
            if (parentVal == -1) {
                roots.push_back(nodeVal);
            } else {
                // 创建父节点（如果不存在）
                if (nodeMap.find(parentVal) == nodeMap.end()) {
                    nodeMap[parentVal] = std::make_shared<TreeNode>(parentVal);
                }
                // 建立父子关系
                nodeMap[parentVal]->addChild(nodeMap[nodeVal]);
            }
        }

        // 将所有根节点添加到森林
        for (int rootVal : roots) {
            forest.addTree(nodeMap[rootVal]);
        }

        return forest;
    }

    /**
     * 获取森林中的连通分量（每棵树的节点集合）
     * @return 连通分量列表，每个集合包含一棵树的所有节点
     */
    std::vector<std::unordered_set<int>> getConnectedComponents() const {
        std::vector<std::unordered_set<int>> components;
        for (const auto& root : trees) {
            std::unordered_set<int> component;
            collectNodes(root, component);
            components.push_back(component);
        }
        return components;
    }

    /**
     * 判断两个节点是否在同一棵树中
     * @param node1Val 第一个节点的值
     * @param node2Val 第二个节点的值
     * @return 如果在同一棵树中返回true，否则返回false
     */
    bool isConnected(int node1Val, int node2Val) const {
        // 先找到两个节点
        auto node1 = findNode(node1Val);
        auto node2 = findNode(node2Val);
        
        if (!node1 || !node2) {
            return false;
        }
        
        // 找到两个节点所在的树的根节点
        int root1 = findRoot(node1Val);
        int root2 = findRoot(node2Val);
        
        return root1 == root2 && root1 != -2; // -2表示节点不存在
    }

    /**
     * 深拷贝森林
     * @return 拷贝后的森林
     */
    Forest clone() const {
        Forest newForest;
        for (const auto& root : trees) {
            newForest.addTree(cloneTree(root));
        }
        return newForest;
    }
};

// 主函数，用于测试
int main() {
    // 创建森林示例
    Forest forest;

    // 创建第一棵树
    //      1
    //     / \
    //    2   3
    //   /
    //  4
    auto tree1 = std::make_shared<TreeNode>(1);
    auto node2 = std::make_shared<TreeNode>(2);
    auto node3 = std::make_shared<TreeNode>(3);
    auto node4 = std::make_shared<TreeNode>(4);
    tree1->addChild(node2);
    tree1->addChild(node3);
    node2->addChild(node4);

    // 创建第二棵树
    //      5
    //     / \
    //    6   7
    auto tree2 = std::make_shared<TreeNode>(5);
    auto node6 = std::make_shared<TreeNode>(6);
    auto node7 = std::make_shared<TreeNode>(7);
    tree2->addChild(node6);
    tree2->addChild(node7);

    // 创建第三棵树
    //      8
    auto tree3 = std::make_shared<TreeNode>(8);

    // 添加树到森林
    forest.addTree(tree1);
    forest.addTree(tree2);
    forest.addTree(tree3);

    // 打印森林
    std::cout << "初始森林：" << std::endl;
    forest.printForest();

    // 测试树的数量
    std::cout << "\n森林中树的数量: " << forest.getTreeCount() << std::endl;

    // 测试节点总数
    std::cout << "森林中节点总数: " << forest.getTotalNodeCount() << std::endl;

    // 测试总高度
    std::cout << "森林中所有树的高度之和: " << forest.getTotalHeight() << std::endl;

    // 测试查找节点
    auto found = forest.findNode(6);
    std::cout << "查找节点6: " << (found ? "找到" : "未找到") << std::endl;

    // 测试合并树
    std::cout << "\n合并第一棵树和第二棵树后：" << std::endl;
    forest.mergeTrees(tree1, tree2);
    forest.printForest();

    // 测试移除树
    std::cout << "\n移除第三棵树后：" << std::endl;
    forest.removeTree(tree3);
    forest.printForest();

    // 测试层序遍历
    std::cout << "\n森林的层序遍历：" << std::endl;
    auto levelOrder = forest.levelOrderTraversal();
    for (const auto& level : levelOrder) {
        std::cout << "[";
        for (size_t i = 0; i < level.size(); ++i) {
            std::cout << level[i];
            if (i < level.size() - 1) {
                std::cout << ", ";
            }
        }
        std::cout << "]" << std::endl;
    }

    // 测试并查集转换
    std::cout << "\n转换为并查集：" << std::endl;
    auto unionFind = forest.toUnionFind();
    for (const auto& [nodeVal, parentVal] : unionFind) {
        std::cout << "节点 " << nodeVal << " 的父节点: " << parentVal << std::endl;
    }

    // 测试从并查集构建森林
    std::cout << "\n从并查集构建森林：" << std::endl;
    auto rebuiltForest = Forest::fromUnionFind(unionFind);
    rebuiltForest.printForest();

    // 测试连通分量
    std::cout << "\n连通分量：" << std::endl;
    auto components = forest.getConnectedComponents();
    for (size_t i = 0; i < components.size(); ++i) {
        std::cout << "连通分量 " << (i + 1) << ": ";
        bool first = true;
        for (int val : components[i]) {
            if (!first) {
                std::cout << ", ";
            }
            std::cout << val;
            first = false;
        }
        std::cout << std::endl;
    }

    // 测试判断连通性
    std::cout << "\n节点1和节点6是否连通: " << (forest.isConnected(1, 6) ? "是" : "否") << std::endl;
    std::cout << "节点4和节点7是否连通: " << (forest.isConnected(4, 7) ? "是" : "否") << std::endl;

    return 0;
}