// C++实现霍夫曼树（Huffman Tree）数据结构
// 霍夫曼树是一种带权路径长度最短的二叉树，也称为最优二叉树

/**
 * 常见应用场景：
 * 1. 数据压缩（霍夫曼编码）
 * 2. 文件压缩算法（如ZIP、GZIP等）
 * 3. 信息论中的最优编码
 * 4. 通讯中的数据传输优化
 * 5. 频率相关的排序和检索
 *
 * 相关算法题目：
 * - LeetCode 1161. 最大层内元素和 https://leetcode.cn/problems/maximum-level-sum-of-a-binary-tree/
 * - LeetCode 1676. 二叉树的最近公共祖先 IV https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree-iv/
 * - LeetCode 199. 二叉树的右视图 https://leetcode.cn/problems/binary-tree-right-side-view/
 * - LintCode 913. 二叉树的序列化与反序列化 https://www.lintcode.com/problem/913/
 * - 洛谷 P1090 合并果子 https://www.luogu.com.cn/problem/P1090
 * - 牛客 NC139 数据流中的中位数 https://www.nowcoder.com/practice/9be0172896bd43948f8a32fb954e1be1
 * - HackerRank Huffman Coding https://www.hackerrank.com/challenges/tree-huffman-decoding/problem
 * - CodeChef HUFFMAN https://www.codechef.com/problems/HUFFMAN
 * - USACO Huffman Coding https://usaco.org/index.php?page=viewproblem2&cpid=689
 * - AtCoder ABC240 E - Ranges on Tree https://atcoder.jp/contests/abc240/tasks/abc240_e
 * - 杭电 OJ 1053 Entropy https://acm.hdu.edu.cn/showproblem.php?pid=1053
 * - SPOJ ADACOINS https://www.spoj.com/problems/ADACOINS/
 * - Codeforces 1278 F. Cards https://codeforces.com/problemset/problem/1278/F
 */

#include <iostream>
#include <vector>
#include <queue>
#include <unordered_map>
#include <memory>
#include <string>
#include <algorithm>
#include <stdexcept>
#include <deque>

// 霍夫曼树节点类
class HuffmanNode {
public:
    char data;       // 字符数据
    int frequency;   // 出现频率
    std::shared_ptr<HuffmanNode> left;  // 左子节点
    std::shared_ptr<HuffmanNode> right; // 右子节点

    /**
     * 构造函数
     * @param data 字符数据
     * @param frequency 出现频率
     */
    explicit HuffmanNode(char data, int frequency) 
        : data(data), frequency(frequency), left(nullptr), right(nullptr) {}

    /**
     * 构造函数（用于内部节点）
     * @param frequency 出现频率
     */
    explicit HuffmanNode(int frequency) 
        : data('\0'), frequency(frequency), left(nullptr), right(nullptr) {}
};

// 用于优先队列的比较函数（最小堆）
struct CompareNodes {
    bool operator()(const std::shared_ptr<HuffmanNode>& a, const std::shared_ptr<HuffmanNode>& b) {
        return a->frequency > b->frequency; // 小顶堆
    }
};

// 霍夫曼树类
class HuffmanTree {
private:
    std::shared_ptr<HuffmanNode> root; // 霍夫曼树的根节点

    /**
     * 递归生成霍夫曼编码
     * @param node 当前节点
     * @param currentCode 当前编码
     * @param codes 编码映射表
     */
    void generateCodes(const std::shared_ptr<HuffmanNode>& node, const std::string& currentCode, 
                      std::unordered_map<char, std::string>& codes) const {
        if (!node) {
            return;
        }

        // 如果是叶子节点，保存编码
        if (!node->left && !node->right) {
            codes[node->data] = currentCode.empty() ? "0" : currentCode;
            return;
        }

        // 递归遍历左右子树
        generateCodes(node->left, currentCode + "0", codes);
        generateCodes(node->right, currentCode + "1", codes);
    }

    /**
     * 递归计算带权路径长度
     * @param node 当前节点
     * @param depth 当前深度
     * @return 带权路径长度
     */
    int calculateWPL(const std::shared_ptr<HuffmanNode>& node, int depth) const {
        if (!node) {
            return 0;
        }

        // 叶子节点
        if (!node->left && !node->right) {
            return node->frequency * depth;
        }

        // 内部节点，递归计算左右子树
        return calculateWPL(node->left, depth + 1) + calculateWPL(node->right, depth + 1);
    }

    /**
     * 递归打印树结构
     * @param node 当前节点
     * @param level 当前层级
     */
    void printTree(const std::shared_ptr<HuffmanNode>& node, int level) const {
        if (!node) {
            return;
        }

        // 打印右子树
        printTree(node->right, level + 1);

        // 打印当前节点
        for (int i = 0; i < level; i++) {
            std::cout << "    ";
        }
        if (node->data == '\0') {
            std::cout << "[内部] " << node->frequency << std::endl;
        } else {
            std::cout << "['" << node->data << "'] " << node->frequency << std::endl;
        }

        // 打印左子树
        printTree(node->left, level + 1);
    }

    /**
     * 递归计算树的高度
     * @param node 当前节点
     * @return 树的高度
     */
    int getHeight(const std::shared_ptr<HuffmanNode>& node) const {
        if (!node) {
            return 0;
        }
        int leftHeight = getHeight(node->left);
        int rightHeight = getHeight(node->right);
        return std::max(leftHeight, rightHeight) + 1;
    }

    /**
     * 递归统计叶子节点数量
     * @param node 当前节点
     * @return 叶子节点数量
     */
    int getLeafCount(const std::shared_ptr<HuffmanNode>& node) const {
        if (!node) {
            return 0;
        }
        if (!node->left && !node->right) {
            return 1;
        }
        return getLeafCount(node->left) + getLeafCount(node->right);
    }

    /**
     * 递归比较两棵子树是否相同
     * @param node1 第一棵树的节点
     * @param node2 第二棵树的节点
     * @return 如果相同返回true，否则返回false
     */
    bool equals(const std::shared_ptr<HuffmanNode>& node1, const std::shared_ptr<HuffmanNode>& node2) const {
        if (!node1 && !node2) {
            return true;
        }
        if (!node1 || !node2) {
            return false;
        }

        // 比较当前节点的频率
        if (node1->frequency != node2->frequency) {
            return false;
        }

        // 如果是叶子节点，还需要比较字符
        if (!node1->left && !node1->right && !node2->left && !node2->right) {
            return node1->data == node2->data;
        }

        // 递归比较左右子树
        return equals(node1->left, node2->left) && equals(node1->right, node2->right);
    }

    /**
     * 递归收集字符频率
     * @param node 当前节点
     * @param frequencies 频率映射表
     */
    void collectFrequencies(const std::shared_ptr<HuffmanNode>& node, 
                           std::unordered_map<char, int>& frequencies) const {
        if (!node) {
            return;
        }

        if (!node->left && !node->right) {
            frequencies[node->data] = node->frequency;
        } else {
            collectFrequencies(node->left, frequencies);
            collectFrequencies(node->right, frequencies);
        }
    }

    /**
     * 递归构建DOT表示
     * @param node 当前节点
     * @param dot DOT命令列表
     * @return 节点ID
     */
    std::string buildDot(const std::shared_ptr<HuffmanNode>& node, std::vector<std::string>& dot) const {
        if (!node) {
            return "null";
        }

        std::string nodeId = "node_" + std::to_string(reinterpret_cast<uintptr_t>(node.get()));
        std::string label;
        if (node->data == '\0') {
            label = std::to_string(node->frequency);
        } else {
            label = node->data + std::string(":") + std::to_string(node->frequency);
        }
        dot.push_back("  " + nodeId + " [label=\"" + label + \"];");

        if (node->left) {
            std::string leftId = buildDot(node->left, dot);
            dot.push_back("  " + nodeId + " -> " + leftId + " [label=\"0\"];");
        }

        if (node->right) {
            std::string rightId = buildDot(node->right, dot);
            dot.push_back("  " + nodeId + " -> " + rightId + " [label=\"1\"];");
        }

        return nodeId;
    }

public:
    /**
     * 构造函数，通过字符频率表构建霍夫曼树
     * @param frequencyMap 字符频率映射表
     */
    explicit HuffmanTree(const std::unordered_map<char, int>& frequencyMap) {
        buildTree(frequencyMap);
    }

    /**
     * 默认构造函数
     */
    HuffmanTree() : root(nullptr) {}

    /**
     * 析构函数
     */
    ~HuffmanTree() = default;

    /**
     * 构建霍夫曼树
     * @param frequencyMap 字符频率映射表
     */
    void buildTree(const std::unordered_map<char, int>& frequencyMap) {
        // 创建优先队列（最小堆），按照频率排序
        std::priority_queue<std::shared_ptr<HuffmanNode>, 
                          std::vector<std::shared_ptr<HuffmanNode>>, 
                          CompareNodes> pq;

        // 将所有字符节点加入优先队列
        for (const auto& entry : frequencyMap) {
            pq.push(std::make_shared<HuffmanNode>(entry.first, entry.second));
        }

        // 构建霍夫曼树
        while (pq.size() > 1) {
            // 取出两个最小频率的节点
            auto left = pq.top();
            pq.pop();
            auto right = pq.top();
            pq.pop();

            // 创建新的内部节点，频率为两个子节点的频率之和
            auto internalNode = std::make_shared<HuffmanNode>(left->frequency + right->frequency);
            internalNode->left = left;
            internalNode->right = right;

            // 将内部节点加入队列
            pq.push(internalNode);
        }

        // 队中只剩下根节点
        if (!pq.empty()) {
            root = pq.top();
            pq.pop();
        }
    }

    /**
     * 获取霍夫曼编码表
     * @return 字符到霍夫曼编码的映射
     */
    std::unordered_map<char, std::string> getHuffmanCodes() const {
        std::unordered_map<char, std::string> codes;
        if (root) {
            generateCodes(root, "", codes);
        }
        return codes;
    }

    /**
     * 编码文本
     * @param text 原始文本
     * @return 霍夫曼编码后的二进制字符串
     */
    std::string encode(const std::string& text) const {
        auto codes = getHuffmanCodes();
        std::string encoded;

        for (char c : text) {
            auto it = codes.find(c);
            if (it != codes.end()) {
                encoded += it->second;
            } else {
                throw std::invalid_argument(std::string("字符 ") + c + " 不在霍夫曼树中");
            }
        }

        return encoded;
    }

    /**
     * 解码二进制字符串
     * @param encoded 霍夫曼编码的二进制字符串
     * @return 解码后的文本
     */
    std::string decode(const std::string& encoded) const {
        if (!root) {
            throw std::invalid_argument("霍夫曼树为空");
        }

        std::string decoded;
        auto current = root;

        for (char bit : encoded) {
            if (bit == '0') {
                current = current->left;
            } else if (bit == '1') {
                current = current->right;
            } else {
                throw std::invalid_argument(std::string("无效的二进制位: ") + bit);
            }

            if (!current) {
                throw std::invalid_argument("无效的霍夫曼编码");
            }

            // 到达叶子节点
            if (!current->left && !current->right) {
                decoded += current->data;
                current = root; // 重置到根节点
            }
        }

        return decoded;
    }

    /**
     * 计算霍夫曼树的带权路径长度（WPL）
     * @return 带权路径长度
     */
    int calculateWPL() const {
        return calculateWPL(root, 0);
    }

    /**
     * 打印霍夫曼树的结构
     */
    void printTree() const {
        std::cout << "霍夫曼树结构：" << std::endl;
        printTree(root, 0);
    }

    /**
     * 获取树的高度
     * @return 树的高度
     */
    int getHeight() const {
        return getHeight(root);
    }

    /**
     * 统计叶子节点数量
     * @return 叶子节点数量
     */
    int getLeafCount() const {
        return getLeafCount(root);
    }

    /**
     * 根据文本自动构建频率表并创建霍夫曼树
     * @param text 输入文本
     * @return 构建的霍夫曼树
     */
    static HuffmanTree buildFromText(const std::string& text) {
        // 统计字符频率
        std::unordered_map<char, int> frequencyMap;
        for (char c : text) {
            frequencyMap[c]++;
        }
        return HuffmanTree(frequencyMap);
    }

    /**
     * 获取根节点
     * @return 根节点
     */
    std::shared_ptr<HuffmanNode> getRoot() const {
        return root;
    }

    /**
     * 检查两棵霍夫曼树是否相同
     * @param other 另一棵霍夫曼树
     * @return 如果相同返回true，否则返回false
     */
    bool equals(const HuffmanTree& other) const {
        return equals(root, other.getRoot());
    }

    /**
     * 层序遍历霍夫曼树
     * @return 层序遍历结果
     */
    std::vector<std::vector<std::string>> levelOrderTraversal() const {
        std::vector<std::vector<std::string>> result;
        if (!root) {
            return result;
        }

        std::queue<std::shared_ptr<HuffmanNode>> queue;
        queue.push(root);

        while (!queue.empty()) {
            int levelSize = queue.size();
            std::vector<std::string> currentLevel;

            for (int i = 0; i < levelSize; i++) {
                auto node = queue.front();
                queue.pop();
                if (node->data == '\0') {
                    currentLevel.push_back("[内部] " + std::to_string(node->frequency));
                } else {
                    currentLevel.push_back("['" + std::string(1, node->data) + "'] " + std::to_string(node->frequency));
                }

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
     * 计算压缩率
     * @param originalText 原始文本
     * @return 压缩率（压缩后大小/原始大小）
     */
    double getCompressionRatio(const std::string& originalText) const {
        // 假设原始文本每个字符占用8位
        int originalSize = originalText.length() * 8;
        
        // 计算压缩后的大小（以位为单位）
        std::string encoded = encode(originalText);
        int compressedSize = encoded.length();
        
        return originalSize > 0 ? static_cast<double>(compressedSize) / originalSize : 0.0;
    }

    /**
     * 获取字符频率表
     * @return 字符频率映射
     */
    std::unordered_map<char, int> getCharFrequencies() const {
        std::unordered_map<char, int> frequencies;
        collectFrequencies(root, frequencies);
        return frequencies;
    }

    /**
     * 生成DOT格式的树表示（用于可视化）
     * @return DOT格式的字符串
     */
    std::string toDotFormat() const {
        std::vector<std::string> dot;
        dot.push_back("digraph HuffmanTree {");
        dot.push_back("  node [shape=box];");
        buildDot(root, dot);
        dot.push_back("}");
        
        std::string result;
        for (const auto& line : dot) {
            if (!result.empty()) {
                result += "\n";
            }
            result += line;
        }
        return result;
    }
};

// 主函数，用于测试
int main() {
    // 测试数据：字符及其频率
    std::unordered_map<char, int> frequencyMap = {
        {'a', 5},
        {'b', 9},
        {'c', 12},
        {'d', 13},
        {'e', 16},
        {'f', 45}
    };

    // 创建霍夫曼树
    HuffmanTree huffmanTree(frequencyMap);

    // 打印树结构
    huffmanTree.printTree();

    // 获取霍夫曼编码
    auto codes = huffmanTree.getHuffmanCodes();
    std::cout << "\n霍夫曼编码：" << std::endl;
    for (const auto& [char_, code] : codes) {
        std::cout << char_ << ": " << code << std::endl;
    }

    // 计算WPL
    std::cout << "\n带权路径长度(WPL): " << huffmanTree.calculateWPL() << std::endl;

    // 计算树高和叶子节点数
    std::cout << "树高: " << huffmanTree.getHeight() << std::endl;
    std::cout << "叶子节点数: " << huffmanTree.getLeafCount() << std::endl;

    // 层序遍历
    std::cout << "\n层序遍历：" << std::endl;
    auto levelOrder = huffmanTree.levelOrderTraversal();
    for (size_t i = 0; i < levelOrder.size(); i++) {
        std::cout << "层 " << (i + 1) << ": ";
        for (size_t j = 0; j < levelOrder[i].size(); j++) {
            std::cout << levelOrder[i][j];
            if (j < levelOrder[i].size() - 1) {
                std::cout << ", ";
            }
        }
        std::cout << std::endl;
    }

    // 测试编码和解码
    std::string text = "abcdef";
    try {
        std::string encoded = huffmanTree.encode(text);
        std::string decoded = huffmanTree.decode(encoded);
        
        std::cout << "\n原始文本: " << text << std::endl;
        std::cout << "编码后: " << encoded << std::endl;
        std::cout << "解码后: " << decoded << std::endl;
        std::cout << "编码解码一致性: " << (text == decoded ? "是" : "否") << std::endl;
        std::cout << "压缩率: " << huffmanTree.getCompressionRatio(text) << std::endl;
    } catch (const std::exception& e) {
        std::cout << "错误: " << e.what() << std::endl;
    }

    // 测试从文本构建霍夫曼树
    std::cout << "\n从文本构建霍夫曼树：" << std::endl;
    std::string testText = "hello huffman coding!";
    HuffmanTree treeFromText = HuffmanTree::buildFromText(testText);
    auto codesFromText = treeFromText.getHuffmanCodes();
    std::cout << "文本霍夫曼编码：" << std::endl;
    for (const auto& [char_, code] : codesFromText) {
        std::string displayChar = (char_ == ' ') ? "空格" : std::string(1, char_);
        std::cout << "'" << displayChar << "': " << code << std::endl;
    }
    
    // 测试编码解码
    try {
        std::string encodedText = treeFromText.encode(testText);
        std::string decodedText = treeFromText.decode(encodedText);
        std::cout << "\n原始文本长度: " << testText.length() << " 字符" << std::endl;
        std::cout << "编码后长度: " << encodedText.length() << " 位" << std::endl;
        std::cout << "解码后: " << decodedText << std::endl;
        std::cout << "解码正确性: " << (testText == decodedText ? "是" : "否") << std::endl;
        std::cout << "压缩率: " << treeFromText.getCompressionRatio(testText) << std::endl;
    } catch (const std::exception& e) {
        std::cout << "错误: " << e.what() << std::endl;
    }

    // 测试边界情况
    std::cout << "\n测试边界情况：" << std::endl;
    // 单个字符的情况
    std::string singleCharText = "aaaaa";
    HuffmanTree singleTree = HuffmanTree::buildFromText(singleCharText);
    try {
        std::string singleEncoded = singleTree.encode(singleCharText);
        std::string singleDecoded = singleTree.decode(singleEncoded);
        std::cout << "单字符文本编码解码: " << (singleCharText == singleDecoded ? "成功" : "失败") << std::endl;
        std::cout << "单字符编码: " << singleEncoded << std::endl;
    } catch (const std::exception& e) {
        std::cout << "错误: " << e.what() << std::endl;
    }

    return 0;
}