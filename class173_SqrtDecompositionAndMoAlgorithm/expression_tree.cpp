#include <iostream>
#include <string>
#include <vector>
#include <stack>
#include <queue>
#include <memory>
#include <cmath>
#include <sstream>
#include <stdexcept>
#include <iomanip>
#include <algorithm>

/**
 * 表达式树（Expression Tree）实现
 * 表达式树是一种用于表示数学表达式的二叉树
 * 其中内部节点表示运算符，叶节点表示操作数
 * 
 * 常见应用场景：
 * 1. 表达式计算与求值
 * 2. 表达式简化
 * 3. 表达式转换（中缀转后缀/前缀）
 * 4. 编译器和解释器中的语法树
 * 5. 数学表达式的可视化
 * 6. 布尔表达式的表示和求值
 * 7. 科学计算和计算器应用
 * 
 * 相关算法题目：
 * - LeetCode 150. 逆波兰表达式求值 https://leetcode.cn/problems/evaluate-reverse-polish-notation/
 * - LeetCode 224. 基本计算器 https://leetcode.cn/problems/basic-calculator/
 * - LeetCode 227. 基本计算器 II https://leetcode.cn/problems/basic-calculator-ii/
 * - LeetCode 772. 基本计算器 III https://leetcode.cn/problems/basic-calculator-iii/
 * - LintCode 366. 斐波纳契数列 https://www.lintcode.com/problem/366/
 * - 牛客 NC46 加起来和为目标值的组合 https://www.nowcoder.com/practice/75e6cd5b85ab41c6a7c43359a74e869a
 * - HackerRank Expression Evaluation https://www.hackerrank.com/challenges/expression-evaluation/problem
 * - CodeChef SNAKEEAT https://www.codechef.com/problems/SNAKEEAT
 * - USACO Section 3.4 The Primes https://usaco.org/index.php?page=viewproblem2&cpid=349
 * - AtCoder ABC182 E - Akari https://atcoder.jp/contests/abc182/tasks/abc182_e
 * - 杭电 OJ 1237 简单计算器 https://acm.hdu.edu.cn/showproblem.php?pid=1237
 * - SPOJ ONP - Transform the Expression https://www.spoj.com/problems/ONP/
 * - Codeforces 1077C - Good Array https://codeforces.com/problemset/problem/1077/C
 */

// 表达式树节点类
class TreeNode {
public:
    std::string value;       // 节点值：运算符或操作数
    std::shared_ptr<TreeNode> left;      // 左子节点
    std::shared_ptr<TreeNode> right;     // 右子节点
    bool isOperator;         // 是否为运算符
    
    /**
     * 构造函数
     * @param val 节点值
     */
    TreeNode(const std::string& val) : value(val), left(nullptr), right(nullptr) {
        // 判断是否为运算符（+、-、*、/、^）
        isOperator = "+-*/^".find(value) != std::string::npos;
    }
    
    /**
     * 判断是否为叶子节点（操作数）
     * @return 是否为叶子节点
     */
    bool isLeaf() const {
        return left == nullptr && right == nullptr;
    }
    
    /**
     * 字符串表示
     * @return 节点值的字符串
     */
    std::string toString() const {
        return value;
    }
};

// 表达式树类
class ExpressionTree {
private:
    std::shared_ptr<TreeNode> root;  // 表达式树的根节点
    
    /**
     * 表达式分词处理
     * @param expression 表达式字符串
     * @return 分词后的标记向量
     */
    std::vector<std::string> tokenizeExpression(const std::string& expression) {
        std::vector<std::string> tokens;
        std::string currentToken;
        
        for (char c : expression) {
            // 跳过空白字符
            if (std::isspace(c)) {
                if (!currentToken.empty()) {
                    tokens.push_back(currentToken);
                    currentToken.clear();
                }
                continue;
            }
            
            // 处理运算符和括号
            if ("+-*/^()".find(c) != std::string::npos) {
                if (!currentToken.empty()) {
                    tokens.push_back(currentToken);
                    currentToken.clear();
                }
                tokens.push_back(std::string(1, c));
            } else {
                // 处理数字或变量
                currentToken += c;
            }
        }
        
        // 添加最后一个标记
        if (!currentToken.empty()) {
            tokens.push_back(currentToken);
        }
        
        return tokens;
    }
    
    /**
     * 获取运算符的优先级
     * @param op 运算符
     * @return 优先级值（数字越大优先级越高）
     */
    int getPrecedence(const std::string& op) {
        if (op == "+") return 1;
        if (op == "-") return 1;
        if (op == "*") return 2;
        if (op == "/") return 2;
        if (op == "^") return 3;
        return 0;
    }
    
    /**
     * 中缀表达式转换为后缀表达式
     * @param infix 中缀表达式
     * @return 后缀表达式字符串
     */
    std::string infixToPostfix(const std::string& infix) {
        std::vector<std::string> tokens = tokenizeExpression(infix);
        std::vector<std::string> postfixTokens;
        std::stack<std::string> stack;
        
        for (const std::string& token : tokens) {
            // 操作数直接添加到结果
            if ("+-*/^()".find(token) == std::string::npos) {
                postfixTokens.push_back(token);
            }
            // 左括号入栈
            else if (token == "(") {
                stack.push(token);
            }
            // 处理右括号
            else if (token == ")") {
                while (!stack.empty() && stack.top() != "(") {
                    postfixTokens.push_back(stack.top());
                    stack.pop();
                }
                if (!stack.empty() && stack.top() == "(") {
                    stack.pop();  // 弹出左括号
                } else {
                    throw std::invalid_argument("括号不匹配的表达式: " + infix);
                }
            }
            // 处理运算符
            else {
                while (!stack.empty() && stack.top() != "(" && 
                       getPrecedence(stack.top()) >= getPrecedence(token)) {
                    postfixTokens.push_back(stack.top());
                    stack.pop();
                }
                stack.push(token);
            }
        }
        
        // 将栈中剩余的运算符添加到结果
        while (!stack.empty()) {
            if (stack.top() == "(") {
                throw std::invalid_argument("括号不匹配的表达式: " + infix);
            }
            postfixTokens.push_back(stack.top());
            stack.pop();
        }
        
        // 构建后缀表达式字符串
        std::stringstream ss;
        for (size_t i = 0; i < postfixTokens.size(); ++i) {
            ss << postfixTokens[i];
            if (i < postfixTokens.size() - 1) {
                ss << " ";
            }
        }
        
        return ss.str();
    }
    
    /**
     * 递归计算表达式树的值
     * @param node 当前节点
     * @return 计算结果
     */
    double evaluateRecursive(const std::shared_ptr<TreeNode>& node) {
        // 空节点，返回0
        if (!node) {
            return 0.0;
        }
        
        // 叶节点是操作数，直接转换为数值
        if (node->isLeaf()) {
            try {
                return std::stod(node->value);
            } catch (const std::invalid_argument& e) {
                throw std::invalid_argument("无法解析的操作数: " + node->value);
            } catch (const std::out_of_range& e) {
                throw std::out_of_range("数值超出范围: " + node->value);
            }
        }
        
        // 递归计算左右子树的值
        double leftVal = evaluateRecursive(node->left);
        double rightVal = evaluateRecursive(node->right);
        
        // 根据运算符进行计算
        if (node->value == "+") {
            return leftVal + rightVal;
        } else if (node->value == "-") {
            return leftVal - rightVal;
        } else if (node->value == "*") {
            return leftVal * rightVal;
        } else if (node->value == "/") {
            if (std::abs(rightVal) < 1e-10) {
                throw std::runtime_error("除零错误");
            }
            return leftVal / rightVal;
        } else if (node->value == "^") {
            return std::pow(leftVal, rightVal);
        } else {
            throw std::runtime_error("未知的运算符: " + node->value);
        }
    }
    
    /**
     * 递归生成中缀表达式
     * @param node 当前节点
     * @param result 结果字符串构建器
     */
    void toInfixNotationRecursive(const std::shared_ptr<TreeNode>& node, std::stringstream& result) {
        if (!node) {
            return;
        }
        
        // 对于非叶节点（运算符），需要添加括号
        if (!node->isLeaf()) {
            result << "(";
        }
        
        // 递归处理左子树
        toInfixNotationRecursive(node->left, result);
        
        // 添加当前节点值
        result << node->value;
        
        // 递归处理右子树
        toInfixNotationRecursive(node->right, result);
        
        // 对于非叶节点（运算符），需要添加右括号
        if (!node->isLeaf()) {
            result << ")";
        }
    }
    
    /**
     * 递归生成后缀表达式（后续遍历）
     * @param node 当前节点
     * @param result 结果字符串构建器
     */
    void toPostfixNotationRecursive(const std::shared_ptr<TreeNode>& node, std::stringstream& result) {
        if (!node) {
            return;
        }
        
        // 递归处理左右子树
        toPostfixNotationRecursive(node->left, result);
        toPostfixNotationRecursive(node->right, result);
        
        // 添加当前节点值
        result << node->value << " ";
    }
    
    /**
     * 递归生成前缀表达式（前序遍历）
     * @param node 当前节点
     * @param result 结果字符串构建器
     */
    void toPrefixNotationRecursive(const std::shared_ptr<TreeNode>& node, std::stringstream& result) {
        if (!node) {
            return;
        }
        
        // 添加当前节点值
        result << node->value << " ";
        
        // 递归处理左右子树
        toPrefixNotationRecursive(node->left, result);
        toPrefixNotationRecursive(node->right, result);
    }
    
    /**
     * 递归打印树结构
     * @param node 当前节点
     * @param level 当前节点深度
     */
    void printTreeRecursive(const std::shared_ptr<TreeNode>& node, int level) {
        if (!node) {
            return;
        }
        
        // 先打印右子树（在上层）
        printTreeRecursive(node->right, level + 1);
        
        // 打印当前节点
        for (int i = 0; i < level; ++i) {
            std::cout << "    ";
        }
        std::cout << node->value << std::endl;
        
        // 打印左子树（在下层）
        printTreeRecursive(node->left, level + 1);
    }
    
    /**
     * 递归计算树的高度
     * @param node 当前节点
     * @return 以该节点为根的子树高度
     */
    int getHeightRecursive(const std::shared_ptr<TreeNode>& node) {
        if (!node) {
            return 0;
        }
        
        int leftHeight = getHeightRecursive(node->left);
        int rightHeight = getHeightRecursive(node->right);
        
        return std::max(leftHeight, rightHeight) + 1;
    }
    
    /**
     * 递归计算节点数量
     * @param node 当前节点
     * @return 以该节点为根的子树节点数量
     */
    int getNodeCountRecursive(const std::shared_ptr<TreeNode>& node) {
        if (!node) {
            return 0;
        }
        
        return getNodeCountRecursive(node->left) + 
               getNodeCountRecursive(node->right) + 1;
    }
    
    /**
     * 递归计算叶节点数量
     * @param node 当前节点
     * @return 以该节点为根的子树叶节点数量
     */
    int getLeafCountRecursive(const std::shared_ptr<TreeNode>& node) {
        if (!node) {
            return 0;
        }
        
        if (node->isLeaf()) {
            return 1;
        }
        
        return getLeafCountRecursive(node->left) + 
               getLeafCountRecursive(node->right);
    }
    
    /**
     * 递归计算运算符节点数量
     * @param node 当前节点
     * @return 以该节点为根的子树运算符节点数量
     */
    int getOperatorCountRecursive(const std::shared_ptr<TreeNode>& node) {
        if (!node) {
            return 0;
        }
        
        int count = node->isOperator ? 1 : 0;
        return count + getOperatorCountRecursive(node->left) + 
               getOperatorCountRecursive(node->right);
    }
    
    /**
     * 递归进行前序遍历
     * @param node 当前节点
     * @param result 结果向量
     */
    void preorderTraversalRecursive(const std::shared_ptr<TreeNode>& node, std::vector<std::string>& result) {
        if (!node) {
            return;
        }
        
        result.push_back(node->value);
        preorderTraversalRecursive(node->left, result);
        preorderTraversalRecursive(node->right, result);
    }
    
    /**
     * 递归进行中序遍历
     * @param node 当前节点
     * @param result 结果向量
     */
    void inorderTraversalRecursive(const std::shared_ptr<TreeNode>& node, std::vector<std::string>& result) {
        if (!node) {
            return;
        }
        
        inorderTraversalRecursive(node->left, result);
        result.push_back(node->value);
        inorderTraversalRecursive(node->right, result);
    }
    
    /**
     * 递归进行后序遍历
     * @param node 当前节点
     * @param result 结果向量
     */
    void postorderTraversalRecursive(const std::shared_ptr<TreeNode>& node, std::vector<std::string>& result) {
        if (!node) {
            return;
        }
        
        postorderTraversalRecursive(node->left, result);
        postorderTraversalRecursive(node->right, result);
        result.push_back(node->value);
    }
    
    /**
     * 递归复制节点
     * @param node 要复制的节点
     * @return 复制的节点
     */
    std::shared_ptr<TreeNode> copyNode(const std::shared_ptr<TreeNode>& node) {
        if (!node) {
            return nullptr;
        }
        
        std::shared_ptr<TreeNode> newNode = std::make_shared<TreeNode>(node->value);
        newNode->left = copyNode(node->left);
        newNode->right = copyNode(node->right);
        return newNode;
    }
    
    /**
     * 递归判断两个节点是否相等
     * @param node1 第一个节点
     * @param node2 第二个节点
     * @return 是否相等
     */
    bool equalsNode(const std::shared_ptr<TreeNode>& node1, const std::shared_ptr<TreeNode>& node2) {
        if (!node1 && !node2) {
            return true;
        }
        if (!node1 || !node2) {
            return false;
        }
        
        return (node1->value == node2->value && 
                equalsNode(node1->left, node2->left) && 
                equalsNode(node1->right, node2->right));
    }

public:
    /**
     * 构造函数
     */
    ExpressionTree() : root(nullptr) {}
    
    /**
     * 从后缀表达式构建表达式树
     * @param postfix 后缀表达式
     */
    void buildFromPostfix(const std::string& postfix) {
        // 分词处理
        std::vector<std::string> tokens = tokenizeExpression(postfix);
        std::stack<std::shared_ptr<TreeNode>> stack;
        
        for (const std::string& token : tokens) {
            auto node = std::make_shared<TreeNode>(token);
            
            if (node->isOperator) {
                // 运算符需要两个操作数，从栈中弹出
                if (stack.size() < 2) {
                    throw std::invalid_argument("无效的后缀表达式: " + postfix);
                }
                
                // 注意：先弹出的是右操作数
                node->right = stack.top();
                stack.pop();
                node->left = stack.top();
                stack.pop();
            }
            // 操作数或运算符节点入栈
            stack.push(node);
        }
        
        // 最终栈中应该只有一个节点（根节点）
        if (stack.size() != 1) {
            throw std::invalid_argument("无效的后缀表达式: " + postfix);
        }
        
        root = stack.top();
        stack.pop();
    }
    
    /**
     * 从前缀表达式构建表达式树
     * @param prefix 前缀表达式
     */
    void buildFromPrefix(const std::string& prefix) {
        // 分词处理
        std::vector<std::string> tokens = tokenizeExpression(prefix);
        // 前缀表达式从右往左处理
        std::stack<std::shared_ptr<TreeNode>> stack;
        
        for (auto it = tokens.rbegin(); it != tokens.rend(); ++it) {
            const std::string& token = *it;
            auto node = std::make_shared<TreeNode>(token);
            
            if (node->isOperator) {
                // 运算符需要两个操作数，从栈中弹出
                if (stack.size() < 2) {
                    throw std::invalid_argument("无效的前缀表达式: " + prefix);
                }
                
                // 注意：前缀表达式先弹出的是左操作数
                node->left = stack.top();
                stack.pop();
                node->right = stack.top();
                stack.pop();
            }
            // 操作数或运算符节点入栈
            stack.push(node);
        }
        
        // 最终栈中应该只有一个节点（根节点）
        if (stack.size() != 1) {
            throw std::invalid_argument("无效的前缀表达式: " + prefix);
        }
        
        root = stack.top();
        stack.pop();
    }
    
    /**
     * 从中缀表达式构建表达式树
     * @param infix 中缀表达式
     */
    void buildFromInfix(const std::string& infix) {
        // 中缀表达式转后缀表达式，再构建表达式树
        std::string postfix = infixToPostfix(infix);
        buildFromPostfix(postfix);
    }
    
    /**
     * 计算表达式树的值
     * @return 计算结果
     */
    double evaluate() {
        if (!root) {
            throw std::runtime_error("表达式树为空");
        }
        return evaluateRecursive(root);
    }
    
    /**
     * 获取中缀表达式字符串（带括号）
     * @return 中缀表达式
     */
    std::string toInfixNotation() {
        std::stringstream ss;
        toInfixNotationRecursive(root, ss);
        return ss.str();
    }
    
    /**
     * 获取后缀表达式字符串
     * @return 后缀表达式
     */
    std::string toPostfixNotation() {
        std::stringstream ss;
        toPostfixNotationRecursive(root, ss);
        std::string result = ss.str();
        // 移除最后的空格
        if (!result.empty() && result.back() == ' ') {
            result.pop_back();
        }
        return result;
    }
    
    /**
     * 获取前缀表达式字符串
     * @return 前缀表达式
     */
    std::string toPrefixNotation() {
        std::stringstream ss;
        toPrefixNotationRecursive(root, ss);
        std::string result = ss.str();
        // 移除最后的空格
        if (!result.empty() && result.back() == ' ') {
            result.pop_back();
        }
        return result;
    }
    
    /**
     * 打印表达式树结构
     */
    void printTree() {
        std::cout << "表达式树结构:" << std::endl;
        printTreeRecursive(root, 0);
    }
    
    /**
     * 获取树的高度
     * @return 树的高度
     */
    int getHeight() {
        return getHeightRecursive(root);
    }
    
    /**
     * 获取节点数量
     * @return 节点数量
     */
    int getNodeCount() {
        return getNodeCountRecursive(root);
    }
    
    /**
     * 获取叶节点数量（操作数数量）
     * @return 叶节点数量
     */
    int getLeafCount() {
        return getLeafCountRecursive(root);
    }
    
    /**
     * 获取运算符节点数量
     * @return 运算符节点数量
     */
    int getOperatorCount() {
        return getOperatorCountRecursive(root);
    }
    
    /**
     * 前序遍历表达式树
     * @return 前序遍历结果向量
     */
    std::vector<std::string> preorderTraversal() {
        std::vector<std::string> result;
        preorderTraversalRecursive(root, result);
        return result;
    }
    
    /**
     * 中序遍历表达式树
     * @return 中序遍历结果向量
     */
    std::vector<std::string> inorderTraversal() {
        std::vector<std::string> result;
        inorderTraversalRecursive(root, result);
        return result;
    }
    
    /**
     * 后序遍历表达式树
     * @return 后序遍历结果向量
     */
    std::vector<std::string> postorderTraversal() {
        std::vector<std::string> result;
        postorderTraversalRecursive(root, result);
        return result;
    }
    
    /**
     * 层序遍历表达式树
     * @return 层序遍历结果向量，每个子向量代表一层
     */
    std::vector<std::vector<std::string>> levelOrderTraversal() {
        std::vector<std::vector<std::string>> result;
        if (!root) {
            return result;
        }
        
        std::queue<std::shared_ptr<TreeNode>> queue;
        queue.push(root);
        
        while (!queue.empty()) {
            int levelSize = queue.size();
            std::vector<std::string> currentLevel;
            
            for (int i = 0; i < levelSize; ++i) {
                auto node = queue.front();
                queue.pop();
                currentLevel.push_back(node->value);
                
                if (node->left) {
                    queue.push(node->left);
                }
                if (node->right) {
                    queue.push(node->right);
                }
            }
            
            result.push_back(currentLevel);
        }
        
        return result;
    }
    
    /**
     * 复制表达式树
     * @return 复制的表达式树
     */
    std::shared_ptr<ExpressionTree> copy() {
        auto newTree = std::make_shared<ExpressionTree>();
        newTree->root = copyNode(root);
        return newTree;
    }
    
    /**
     * 判断两个表达式树是否相等
     * @param other 另一个表达式树
     * @return 是否相等
     */
    bool equals(const std::shared_ptr<ExpressionTree>& other) {
        if (!other) {
            return false;
        }
        return equalsNode(root, other->root);
    }
    
    /**
     * 获取根节点
     * @return 根节点
     */
    std::shared_ptr<TreeNode> getRoot() {
        return root;
    }
};

// 用于输出向量的辅助函数
template <typename T>
void printVector(const std::vector<T>& vec, const std::string& separator = " ") {
    for (size_t i = 0; i < vec.size(); ++i) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << separator;
        }
    }
    std::cout << std::endl;
}

// 主函数，用于测试表达式树
int main() {
    try {
        // 创建表达式树实例
        std::shared_ptr<ExpressionTree> tree = std::make_shared<ExpressionTree>();
        
        // 测试从中缀表达式构建
        std::cout << "===== 从中缀表达式构建 =====" << std::endl;
        std::string infixExpression = "3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3";
        tree->buildFromInfix(infixExpression);
        
        // 打印树结构
        tree->printTree();
        
        // 显示不同形式的表达式
        std::cout << "中缀表达式: " << tree->toInfixNotation() << std::endl;
        std::cout << "后缀表达式: " << tree->toPostfixNotation() << std::endl;
        std::cout << "前缀表达式: " << tree->toPrefixNotation() << std::endl;
        
        // 计算表达式值
        double result = tree->evaluate();
        std::cout << "表达式值: " << std::fixed << std::setprecision(6) << result << std::endl;
        
        // 显示树的统计信息
        std::cout << "树高: " << tree->getHeight() << std::endl;
        std::cout << "节点数: " << tree->getNodeCount() << std::endl;
        std::cout << "叶节点数(操作数): " << tree->getLeafCount() << std::endl;
        std::cout << "运算符节点数: " << tree->getOperatorCount() << std::endl;
        
        // 遍历结果
        std::cout << "前序遍历: ";
        printVector(tree->preorderTraversal());
        
        std::cout << "中序遍历: ";
        printVector(tree->inorderTraversal());
        
        std::cout << "后序遍历: ";
        printVector(tree->postorderTraversal());
        
        // 层序遍历
        std::cout << "层序遍历:" << std::endl;
        auto levelOrder = tree->levelOrderTraversal();
        for (size_t i = 0; i < levelOrder.size(); ++i) {
            std::cout << "层 " << (i + 1) << ": ";
            printVector(levelOrder[i]);
        }
        
        // 测试从后缀表达式构建
        std::cout << "\n===== 从后缀表达式构建 =====" << std::endl;
        std::shared_ptr<ExpressionTree> tree2 = std::make_shared<ExpressionTree>();
        std::string postfixExpression = "3 4 2 * 1 5 - 2 3 ^ ^ / +";
        tree2->buildFromPostfix(postfixExpression);
        std::cout << "构建的表达式值: " << std::fixed << std::setprecision(6) << tree2->evaluate() << std::endl;
        std::cout << "两棵树是否相等: " << (tree->equals(tree2) ? "true" : "false") << std::endl;
        
        // 测试从前缀表达式构建
        std::cout << "\n===== 从前缀表达式构建 =====" << std::endl;
        std::shared_ptr<ExpressionTree> tree3 = std::make_shared<ExpressionTree>();
        std::string prefixExpression = "+ 3 / * 4 2 ^ ^ - 1 5 2 3";
        tree3->buildFromPrefix(prefixExpression);
        std::cout << "构建的表达式值: " << std::fixed << std::setprecision(6) << tree3->evaluate() << std::endl;
        std::cout << "与原始树是否相等: " << (tree->equals(tree3) ? "true" : "false") << std::endl;
        
        // 测试表达式复制
        auto copyTree = tree->copy();
        std::cout << "\n===== 表达式树复制 =====" << std::endl;
        std::cout << "复制树的表达式值: " << std::fixed << std::setprecision(6) << copyTree->evaluate() << std::endl;
        std::cout << "与原始树是否相等: " << (tree->equals(copyTree) ? "true" : "false") << std::endl;
        
        // 测试更复杂的表达式
        std::cout << "\n===== 复杂表达式测试 =====" << std::endl;
        std::shared_ptr<ExpressionTree> complexTree = std::make_shared<ExpressionTree>();
        complexTree->buildFromInfix("((2 + 3) * (5 - 2)) / (1 + 2 * 3)");
        std::cout << "复杂表达式值: " << std::fixed << std::setprecision(6) << complexTree->evaluate() << std::endl;
        
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
        return 1;
    }
    
    return 0;
}