#include <iostream>
#include <vector>
#include <string>
#include <map>
#include <unordered_map>
#include <queue>
#include <cmath>
#include <algorithm>
#include <memory>
#include <stdexcept>
#include <iomanip>
#include <sstream>

/**
 * 决策树（Decision Tree）实现
 * 决策树是一种用于分类和回归的监督学习算法
 * 它通过从数据特征中学习决策规则，构建树状模型进行预测
 * 
 * 常见应用场景：
 * 1. 分类问题（如疾病诊断、客户流失预测）
 * 2. 回归问题（如房价预测、销售量预测）
 * 3. 特征重要性分析
 * 4. 规则提取（从树结构中提取业务规则）
 * 5. 数据分析和数据挖掘
 * 6. 风险评估和信用评分
 * 
 * 相关算法题目：
 * - LeetCode 222. 完全二叉树的节点个数 https://leetcode.cn/problems/count-complete-tree-nodes/
 * - LeetCode 637. 二叉树的层平均值 https://leetcode.cn/problems/average-of-levels-in-binary-tree/
 * - LeetCode 572. 另一棵树的子树 https://leetcode.cn/problems/subtree-of-another-tree/
 * - LintCode 95. 验证二叉查找树 https://www.lintcode.com/problem/95/
 * - 洛谷 P3379 【模板】最近公共祖先 https://www.luogu.com.cn/problem/P3379
 * - 牛客 NC119 最小的K个数 https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
 * - HackerRank Tree: Huffman Decoding https://www.hackerrank.com/challenges/tree-huffman-decoding/problem
 * - CodeChef DECMTREE https://www.codechef.com/problems/DECMTREE
 * - USACO Section 3.4 Raucous Rockers https://usaco.org/index.php?page=viewproblem2&cpid=348
 * - AtCoder ABC177 F - I hate Shortest Path Problem https://atcoder.jp/contests/abc177/tasks/abc177_f
 * - 杭电 OJ 2544 最短路 https://acm.hdu.edu.cn/showproblem.php?pid=2544
 * - SPOJ PT07Z - Longest path in a tree https://www.spoj.com/problems/PT07Z/
 * - Codeforces 1327C - Game with Chips https://codeforces.com/problemset/problem/1327/C
 */

// 前向声明
class TreeNode;
class DataRow;

// 用于存储属性值的变体类型
class AttributeValue {
private:
    std::string stringValue;
    double doubleValue;
    bool isDouble;

public:
    AttributeValue(const std::string& value) : stringValue(value), isDouble(false) {}
    AttributeValue(double value) : doubleValue(value), isDouble(true) {}
    
    bool isDoubleType() const { return isDouble; }
    std::string getStringValue() const { return stringValue; }
    double getDoubleValue() const { return doubleValue; }
    
    std::string toString() const {
        if (isDouble) {
            std::stringstream ss;
            ss << doubleValue;
            return ss.str();
        }
        return stringValue;
    }
    
    bool operator==(const AttributeValue& other) const {
        if (isDouble != other.isDouble) return false;
        if (isDouble) {
            return std::abs(doubleValue - other.doubleValue) < 1e-9;
        } else {
            return stringValue == other.stringValue;
        }
    }
};

// 为AttributeValue定义哈希函数，使其可以用作unordered_map的键
namespace std {
    template<>
    struct hash<AttributeValue> {
        size_t operator()(const AttributeValue& val) const {
            if (val.isDoubleType()) {
                // 对double值进行哈希处理
                double d = val.getDoubleValue();
                return hash<double>()(d);
            } else {
                return hash<string>()(val.getStringValue());
            }
        }
    };
}

// 决策树节点类
class TreeNode {
private:
    bool isLeafNode;
    std::string attribute;
    std::unordered_map<std::string, std::shared_ptr<TreeNode>> children;
    std::string classification;
    double value;
    double informationGain;
    int depth;

public:
    // 构造内部节点（用于属性分裂）
    TreeNode(const std::string& attr, double gain, int nodeDepth)
        : isLeafNode(false), attribute(attr), classification(""), value(0.0),
          informationGain(gain), depth(nodeDepth) {}
    
    // 构造叶节点（分类树）
    TreeNode(const std::string& cls, int nodeDepth)
        : isLeafNode(true), attribute(""), classification(cls), value(0.0),
          informationGain(0.0), depth(nodeDepth) {}
    
    // 构造叶节点（回归树）
    TreeNode(double val, int nodeDepth)
        : isLeafNode(true), attribute(""), classification(""), value(val),
          informationGain(0.0), depth(nodeDepth) {}
    
    // Getter方法
    bool isLeaf() const { return isLeafNode; }
    const std::string& getAttribute() const { return attribute; }
    const std::unordered_map<std::string, std::shared_ptr<TreeNode>>& getChildren() const { return children; }
    const std::string& getClassification() const { return classification; }
    double getValue() const { return value; }
    double getInformationGain() const { return informationGain; }
    int getDepth() const { return depth; }
    
    // 添加子节点
    void addChild(const std::string& attributeValue, std::shared_ptr<TreeNode> child) {
        children[attributeValue] = child;
    }
    
    // 字符串表示
    std::string toString() const {
        std::stringstream ss;
        if (isLeafNode) {
            if (!classification.empty()) {
                ss << "叶节点(分类=" << classification << ", 深度=" << depth << ")";
            } else {
                ss << "叶节点(值=" << std::fixed << std::setprecision(4) << value 
                   << ", 深度=" << depth << ")";
            }
        } else {
            ss << "内部节点(属性=" << attribute 
               << ", 信息增益=" << std::fixed << std::setprecision(4) << informationGain 
               << ", 深度=" << depth << ")";
        }
        return ss.str();
    }
};

// 数据集行类
class DataRow {
private:
    std::unordered_map<std::string, AttributeValue> attributes;
    std::string label;
    double value;
    bool isRegressionRow;

public:
    // 构造分类数据行
    DataRow(const std::unordered_map<std::string, AttributeValue>& attrs, const std::string& rowLabel)
        : attributes(attrs), label(rowLabel), value(0.0), isRegressionRow(false) {}
    
    // 构造回归数据行
    DataRow(const std::unordered_map<std::string, AttributeValue>& attrs, double rowValue)
        : attributes(attrs), label(""), value(rowValue), isRegressionRow(true) {}
    
    // 获取属性值
    const AttributeValue* getAttributeValue(const std::string& attribute) const {
        auto it = attributes.find(attribute);
        if (it != attributes.end()) {
            return &(it->second);
        }
        return nullptr;
    }
    
    // 获取所有属性
    const std::unordered_map<std::string, AttributeValue>& getAllAttributes() const {
        return attributes;
    }
    
    // 获取分类标签
    const std::string& getLabel() const {
        return label;
    }
    
    // 获取回归目标值
    double getValue() const {
        return value;
    }
    
    // 判断是否为分类数据
    bool isClassification() const {
        return !isRegressionRow;
    }
    
    // 字符串表示
    std::string toString() const {
        std::stringstream ss;
        ss << "DataRow{attributes={";
        bool first = true;
        for (const auto& attr : attributes) {
            if (!first) ss << ", ";
            ss << attr.first << "=" << attr.second.toString();
            first = false;
        }
        ss << "}, ";
        
        if (isClassification()) {
            ss << "label='" << label << "'";
        } else {
            ss << "value=" << value;
        }
        ss << "}";
        return ss.str();
    }
};

// 决策树类
class DecisionTree {
private:
    std::shared_ptr<TreeNode> root;
    std::vector<std::string> attributes;
    int maxDepth;
    int minSamplesSplit;
    bool isRegression;

    // 判断是否应该停止树的生长
    bool shouldStop(const std::vector<DataRow>& dataSet, 
                   const std::vector<std::string>& availableAttributes, 
                   int depth) const {
        // 1. 数据集为空
        if (dataSet.empty()) {
            return true;
        }
        
        // 2. 达到最大深度
        if (depth >= maxDepth) {
            return true;
        }
        
        // 3. 没有可用属性
        if (availableAttributes.empty()) {
            return true;
        }
        
        // 4. 样本数小于最小分裂样本数
        if (dataSet.size() < minSamplesSplit) {
            return true;
        }
        
        // 5. 所有样本属于同一类别（分类树）
        if (!isRegression) {
            const std::string& firstLabel = dataSet[0].getLabel();
            bool allSame = true;
            for (const auto& row : dataSet) {
                if (row.getLabel() != firstLabel) {
                    allSame = false;
                    break;
                }
            }
            if (allSame) {
                return true;
            }
        }
        // 5. 所有样本的目标值相同（回归树）
        else {
            double firstValue = dataSet[0].getValue();
            bool allSame = true;
            for (const auto& row : dataSet) {
                if (std::abs(row.getValue() - firstValue) >= 1e-9) {
                    allSame = false;
                    break;
                }
            }
            if (allSame) {
                return true;
            }
        }
        
        return false;
    }
    
    // 创建叶节点
    std::shared_ptr<TreeNode> createLeafNode(const std::vector<DataRow>& dataSet, int depth) const {
        if (dataSet.empty()) {
            return isRegression ? 
                   std::make_shared<TreeNode>(0.0, depth) : 
                   std::make_shared<TreeNode>("Unknown", depth);
        }
        
        if (!isRegression) {
            // 分类树：返回最常见的类别
            std::map<std::string, int> labelCounts;
            for (const auto& row : dataSet) {
                labelCounts[row.getLabel()]++;
            }
            
            std::string mostCommonLabel = "Unknown";
            int maxCount = 0;
            for (const auto& pair : labelCounts) {
                if (pair.second > maxCount) {
                    maxCount = pair.second;
                    mostCommonLabel = pair.first;
                }
            }
            
            return std::make_shared<TreeNode>(mostCommonLabel, depth);
        } else {
            // 回归树：返回平均值
            double sum = 0.0;
            for (const auto& row : dataSet) {
                sum += row.getValue();
            }
            double average = sum / dataSet.size();
            
            return std::make_shared<TreeNode>(average, depth);
        }
    }
    
    // 根据属性值分割数据集
    std::map<std::string, std::vector<DataRow>> partitionByAttribute(
            const std::vector<DataRow>& dataSet, const std::string& attribute) const {
        std::map<std::string, std::vector<DataRow>> partitions;
        
        for (const auto& row : dataSet) {
            const AttributeValue* value = row.getAttributeValue(attribute);
            if (value) {
                partitions[value->toString()].push_back(row);
            }
        }
        
        return partitions;
    }
    
    // 计算数据集的熵
    double calculateEntropy(const std::vector<DataRow>& dataSet) const {
        if (dataSet.empty()) {
            return 0.0;
        }
        
        std::map<std::string, int> labelCounts;
        for (const auto& row : dataSet) {
            labelCounts[row.getLabel()]++;
        }
        
        double entropy = 0.0;
        double total = dataSet.size();
        
        for (const auto& pair : labelCounts) {
            double probability = pair.second / total;
            entropy -= probability * std::log2(probability);
        }
        
        return entropy;
    }
    
    // 计算数据集的方差
    double calculateVariance(const std::vector<DataRow>& dataSet) const {
        if (dataSet.empty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (const auto& row : dataSet) {
            sum += row.getValue();
        }
        double mean = sum / dataSet.size();
        
        double variance = 0.0;
        for (const auto& row : dataSet) {
            double diff = row.getValue() - mean;
            variance += diff * diff;
        }
        variance /= dataSet.size();
        
        return variance;
    }
    
    // 计算信息增益（分类树）或方差减少（回归树）
    double calculateGain(const std::vector<DataRow>& dataSet, const std::string& attribute) const {
        if (dataSet.empty()) {
            return 0.0;
        }
        
        if (!isRegression) {
            // 计算信息增益（使用熵）
            double parentEntropy = calculateEntropy(dataSet);
            auto partitions = partitionByAttribute(dataSet, attribute);
            
            double weightedEntropy = 0.0;
            for (const auto& pair : partitions) {
                const auto& subset = pair.second;
                double weight = static_cast<double>(subset.size()) / dataSet.size();
                weightedEntropy += weight * calculateEntropy(subset);
            }
            
            return parentEntropy - weightedEntropy;
        } else {
            // 计算方差减少
            double parentVariance = calculateVariance(dataSet);
            auto partitions = partitionByAttribute(dataSet, attribute);
            
            double weightedVariance = 0.0;
            for (const auto& pair : partitions) {
                const auto& subset = pair.second;
                double weight = static_cast<double>(subset.size()) / dataSet.size();
                weightedVariance += weight * calculateVariance(subset);
            }
            
            return parentVariance - weightedVariance;
        }
    }
    
    // 选择最佳分裂属性
    std::string selectBestAttribute(const std::vector<DataRow>& dataSet, 
                                   const std::vector<std::string>& availableAttributes, 
                                   double& bestGain) const {
        std::string bestAttribute;
        bestGain = -std::numeric_limits<double>::max();
        
        for (const auto& attribute : availableAttributes) {
            double gain = calculateGain(dataSet, attribute);
            if (gain > bestGain) {
                bestGain = gain;
                bestAttribute = attribute;
            }
        }
        
        return bestAttribute;
    }
    
    // 递归构建决策树
    std::shared_ptr<TreeNode> buildTree(const std::vector<DataRow>& dataSet,
                                       std::vector<std::string> availableAttributes,
                                       int depth) {
        // 检查是否满足终止条件
        if (shouldStop(dataSet, availableAttributes, depth)) {
            return createLeafNode(dataSet, depth);
        }
        
        // 选择最佳分裂属性
        double bestGain;
        std::string bestAttribute = selectBestAttribute(dataSet, availableAttributes, bestGain);
        
        // 创建内部节点
        auto node = std::make_shared<TreeNode>(bestAttribute, bestGain, depth);
        
        // 根据最佳属性的值分割数据集
        auto partitions = partitionByAttribute(dataSet, bestAttribute);
        
        // 为每个属性值创建子树
        for (const auto& pair : partitions) {
            const auto& value = pair.first;
            const auto& subset = pair.second;
            
            // 如果子集为空，创建叶节点
            if (subset.empty()) {
                node->addChild(value, createLeafNode(dataSet, depth + 1));
            } else {
                // 递归构建子树
                std::vector<std::string> newAttributes;
                for (const auto& attr : availableAttributes) {
                    if (attr != bestAttribute) {
                        newAttributes.push_back(attr);
                    }
                }
                auto childNode = buildTree(subset, newAttributes, depth + 1);
                node->addChild(value, childNode);
            }
        }
        
        return node;
    }
    
    // 递归预测
    std::pair<std::string, double> predictRecursive(
            const std::shared_ptr<TreeNode>& node, 
            const std::unordered_map<std::string, AttributeValue>& attributes) const {
        // 如果是叶节点，返回分类或回归值
        if (node->isLeaf()) {
            if (!node->getClassification().empty()) {
                return {node->getClassification(), 0.0};
            } else {
                return {"", node->getValue()};
            }
        }
        
        // 获取当前节点用于分裂的属性
        const std::string& splitAttribute = node->getAttribute();
        auto it = attributes.find(splitAttribute);
        
        // 如果找不到对应属性值的子节点，返回默认预测值
        if (it == attributes.end()) {
            return getDefaultPrediction(node);
        }
        
        // 查找对应的子节点
        const auto& children = node->getChildren();
        auto childIt = children.find(it->second.toString());
        
        // 如果找不到子节点，返回默认预测值
        if (childIt == children.end()) {
            return getDefaultPrediction(node);
        }
        
        // 递归到子节点
        return predictRecursive(childIt->second, attributes);
    }
    
    // 获取默认预测值（当无法找到路径时）
    std::pair<std::string, double> getDefaultPrediction(
            const std::shared_ptr<TreeNode>& node) const {
        // 这里简化处理，实际应该根据子节点情况返回最常见的分类或平均值
        return isRegression ? std::make_pair("", 0.0) : std::make_pair("Unknown", 0.0);
    }
    
    // 递归打印树结构
    void printTreeRecursive(const std::shared_ptr<TreeNode>& node, const std::string& prefix) const {
        std::cout << prefix << node->toString() << std::endl;
        
        if (!node->isLeaf()) {
            const auto& children = node->getChildren();
            for (const auto& pair : children) {
                std::cout << prefix << "  |__ " << node->getAttribute() << " = " << pair.first << std::endl;
                printTreeRecursive(pair.second, prefix + "      ");
            }
        }
    }
    
    // 递归计算树高
    int calculateHeight(const std::shared_ptr<TreeNode>& node) const {
        if (!node) {
            return 0;
        }
        
        if (node->isLeaf()) {
            return 1;
        }
        
        int maxChildHeight = 0;
        const auto& children = node->getChildren();
        for (const auto& pair : children) {
            int childHeight = calculateHeight(pair.second);
            maxChildHeight = std::max(maxChildHeight, childHeight);
        }
        
        return maxChildHeight + 1;
    }
    
    // 递归计算节点数量
    int countNodes(const std::shared_ptr<TreeNode>& node) const {
        if (!node) {
            return 0;
        }
        
        int count = 1;  // 当前节点
        const auto& children = node->getChildren();
        for (const auto& pair : children) {
            count += countNodes(pair.second);
        }
        
        return count;
    }
    
    // 递归计算叶节点数量
    int countLeaves(const std::shared_ptr<TreeNode>& node) const {
        if (!node) {
            return 0;
        }
        
        if (node->isLeaf()) {
            return 1;
        }
        
        int leafCount = 0;
        const auto& children = node->getChildren();
        for (const auto& pair : children) {
            leafCount += countLeaves(pair.second);
        }
        
        return leafCount;
    }

public:
    // 构造函数
    DecisionTree(int maxDepth = 10, int minSamplesSplit = 2, bool isRegression = false)
        : maxDepth(maxDepth), minSamplesSplit(minSamplesSplit), 
          isRegression(isRegression), root(nullptr) {}
    
    // 训练决策树
    void fit(const std::vector<DataRow>& dataSet, const std::vector<std::string>& attrs) {
        attributes = attrs;
        root = buildTree(dataSet, attrs, 0);
    }
    
    // 预测单个样本
    std::string predictClassification(const std::unordered_map<std::string, AttributeValue>& attrs) const {
        if (!root) {
            throw std::runtime_error("决策树尚未训练");
        }
        
        if (isRegression) {
            throw std::runtime_error("这是回归树，请使用predictRegression方法");
        }
        
        auto result = predictRecursive(root, attrs);
        return result.first;
    }
    
    // 回归预测
    double predictRegression(const std::unordered_map<std::string, AttributeValue>& attrs) const {
        if (!root) {
            throw std::runtime_error("决策树尚未训练");
        }
        
        if (!isRegression) {
            throw std::runtime_error("这是分类树，请使用predictClassification方法");
        }
        
        auto result = predictRecursive(root, attrs);
        return result.second;
    }
    
    // 批量预测分类
    std::vector<std::string> predictAllClassification(const std::vector<DataRow>& dataSet) const {
        std::vector<std::string> predictions;
        for (const auto& row : dataSet) {
            predictions.push_back(predictClassification(row.getAllAttributes()));
        }
        return predictions;
    }
    
    // 批量预测回归
    std::vector<double> predictAllRegression(const std::vector<DataRow>& dataSet) const {
        std::vector<double> predictions;
        for (const auto& row : dataSet) {
            predictions.push_back(predictRegression(row.getAllAttributes()));
        }
        return predictions;
    }
    
    // 计算分类准确率
    double calculateAccuracy(const std::vector<DataRow>& testData) const {
        if (isRegression) {
            throw std::runtime_error("不能对回归树计算准确率，请使用均方误差等指标");
        }
        
        int correctCount = 0;
        for (const auto& row : testData) {
            std::string prediction = predictClassification(row.getAllAttributes());
            if (prediction == row.getLabel()) {
                correctCount++;
            }
        }
        
        return static_cast<double>(correctCount) / testData.size();
    }
    
    // 计算均方误差（MSE）
    double calculateMSE(const std::vector<DataRow>& testData) const {
        if (!isRegression) {
            throw std::runtime_error("不能对分类树计算均方误差，请使用准确率等指标");
        }
        
        double sumSquaredErrors = 0.0;
        for (const auto& row : testData) {
            double prediction = predictRegression(row.getAllAttributes());
            double actual = row.getValue();
            double error = prediction - actual;
            sumSquaredErrors += error * error;
        }
        
        return sumSquaredErrors / testData.size();
    }
    
    // 打印决策树
    void printTree() const {
        if (!root) {
            std::cout << "决策树为空" << std::endl;
            return;
        }
        
        std::cout << "决策树结构：" << std::endl;
        printTreeRecursive(root, "");
    }
    
    // 获取树的高度
    int getHeight() const {
        return calculateHeight(root);
    }
    
    // 获取节点数量
    int getNodeCount() const {
        return countNodes(root);
    }
    
    // 获取叶节点数量
    int getLeafCount() const {
        return countLeaves(root);
    }
    
    // 获取根节点
    std::shared_ptr<TreeNode> getRoot() const {
        return root;
    }
    
    // 层序遍历决策树
    std::vector<std::vector<std::string>> levelOrderTraversal() const {
        std::vector<std::vector<std::string>> result;
        if (!root) {
            return result;
        }
        
        std::queue<std::shared_ptr<TreeNode>> queue;
        queue.push(root);
        
        while (!queue.empty()) {
            int levelSize = queue.size();
            std::vector<std::string> currentLevel;
            
            for (int i = 0; i < levelSize; i++) {
                auto node = queue.front();
                queue.pop();
                currentLevel.push_back(node->toString());
                
                if (!node->isLeaf()) {
                    const auto& children = node->getChildren();
                    for (const auto& pair : children) {
                        queue.push(pair.second);
                    }
                }
            }
            
            result.push_back(currentLevel);
        }
        
        return result;
    }
};

// 主函数，用于测试决策树
int main() {
    // 创建示例数据（天气数据集 - 分类问题）
    std::vector<DataRow> weatherData;
    std::vector<std::string> attributes = {"Outlook", "Temperature", "Humidity", "Windy"};
    
    // 添加训练数据
    std::unordered_map<std::string, AttributeValue> attr1;
    attr1["Outlook"] = AttributeValue("Sunny");
    attr1["Temperature"] = AttributeValue("Hot");
    attr1["Humidity"] = AttributeValue("High");
    attr1["Windy"] = AttributeValue(false);
    weatherData.emplace_back(attr1, "No");
    
    std::unordered_map<std::string, AttributeValue> attr2;
    attr2["Outlook"] = AttributeValue("Sunny");
    attr2["Temperature"] = AttributeValue("Hot");
    attr2["Humidity"] = AttributeValue("High");
    attr2["Windy"] = AttributeValue(true);
    weatherData.emplace_back(attr2, "No");
    
    std::unordered_map<std::string, AttributeValue> attr3;
    attr3["Outlook"] = AttributeValue("Overcast");
    attr3["Temperature"] = AttributeValue("Hot");
    attr3["Humidity"] = AttributeValue("High");
    attr3["Windy"] = AttributeValue(false);
    weatherData.emplace_back(attr3, "Yes");
    
    std::unordered_map<std::string, AttributeValue> attr4;
    attr4["Outlook"] = AttributeValue("Rainy");
    attr4["Temperature"] = AttributeValue("Mild");
    attr4["Humidity"] = AttributeValue("High");
    attr4["Windy"] = AttributeValue(false);
    weatherData.emplace_back(attr4, "Yes");
    
    std::unordered_map<std::string, AttributeValue> attr5;
    attr5["Outlook"] = AttributeValue("Rainy");
    attr5["Temperature"] = AttributeValue("Cool");
    attr5["Humidity"] = AttributeValue("Normal");
    attr5["Windy"] = AttributeValue(false);
    weatherData.emplace_back(attr5, "Yes");
    
    std::unordered_map<std::string, AttributeValue> attr6;
    attr6["Outlook"] = AttributeValue("Rainy");
    attr6["Temperature"] = AttributeValue("Cool");
    attr6["Humidity"] = AttributeValue("Normal");
    attr6["Windy"] = AttributeValue(true);
    weatherData.emplace_back(attr6, "No");
    
    std::unordered_map<std::string, AttributeValue> attr7;
    attr7["Outlook"] = AttributeValue("Overcast");
    attr7["Temperature"] = AttributeValue("Cool");
    attr7["Humidity"] = AttributeValue("Normal");
    attr7["Windy"] = AttributeValue(true);
    weatherData.emplace_back(attr7, "Yes");
    
    std::unordered_map<std::string, AttributeValue> attr8;
    attr8["Outlook"] = AttributeValue("Sunny");
    attr8["Temperature"] = AttributeValue("Mild");
    attr8["Humidity"] = AttributeValue("High");
    attr8["Windy"] = AttributeValue(false);
    weatherData.emplace_back(attr8, "No");
    
    std::unordered_map<std::string, AttributeValue> attr9;
    attr9["Outlook"] = AttributeValue("Sunny");
    attr9["Temperature"] = AttributeValue("Cool");
    attr9["Humidity"] = AttributeValue("Normal");
    attr9["Windy"] = AttributeValue(false);
    weatherData.emplace_back(attr9, "Yes");
    
    std::unordered_map<std::string, AttributeValue> attr10;
    attr10["Outlook"] = AttributeValue("Rainy");
    attr10["Temperature"] = AttributeValue("Mild");
    attr10["Humidity"] = AttributeValue("Normal");
    attr10["Windy"] = AttributeValue(false);
    weatherData.emplace_back(attr10, "Yes");
    
    // 训练分类决策树
    DecisionTree classifier(10, 2, false);
    classifier.fit(weatherData, attributes);
    
    // 打印决策树
    std::cout << "===== 分类决策树 ======\n" << std::endl;
    classifier.printTree();
    
    // 测试预测
    std::unordered_map<std::string, AttributeValue> testAttr;
    testAttr["Outlook"] = AttributeValue("Sunny");
    testAttr["Temperature"] = AttributeValue("Mild");
    testAttr["Humidity"] = AttributeValue("Normal");
    testAttr["Windy"] = AttributeValue(true);
    
    try {
        std::string prediction = classifier.predictClassification(testAttr);
        std::cout << "\n测试样本预测结果: " << prediction << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "预测错误: " << e.what() << std::endl;
    }
    
    // 模型统计信息
    std::cout << "\n树高: " << classifier.getHeight() << std::endl;
    std::cout << "节点数: " << classifier.getNodeCount() << std::endl;
    std::cout << "叶节点数: " << classifier.getLeafCount() << std::endl;
    
    // 计算训练集准确率
    try {
        double accuracy = classifier.calculateAccuracy(weatherData);
        std::cout << "训练集准确率: " << std::fixed << std::setprecision(2) << accuracy * 100 << "%" << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "计算准确率错误: " << e.what() << std::endl;
    }
    
    // 层序遍历
    std::cout << "\n层序遍历：" << std::endl;
    auto levelOrder = classifier.levelOrderTraversal();
    for (int i = 0; i < levelOrder.size(); i++) {
        std::cout << "层 " << (i + 1) << ": ";
        for (const auto& node : levelOrder[i]) {
            std::cout << node << "; ";
        }
        std::cout << std::endl;
    }
    
    // 创建回归树测试数据
    std::vector<DataRow> regressionData;
    std::vector<std::string> regressionAttrs = {"Size", "Bedrooms", "Bathrooms", "Age"};
    
    // 添加房屋价格预测数据（简化示例）
    std::unordered_map<std::string, AttributeValue> house1;
    house1["Size"] = AttributeValue(1400.0);
    house1["Bedrooms"] = AttributeValue(3.0);
    house1["Bathrooms"] = AttributeValue(2.0);
    house1["Age"] = AttributeValue(10.0);
    regressionData.emplace_back(house1, 200000.0);
    
    std::unordered_map<std::string, AttributeValue> house2;
    house2["Size"] = AttributeValue(1600.0);
    house2["Bedrooms"] = AttributeValue(3.0);
    house2["Bathrooms"] = AttributeValue(2.5);
    house2["Age"] = AttributeValue(5.0);
    regressionData.emplace_back(house2, 250000.0);
    
    std::unordered_map<std::string, AttributeValue> house3;
    house3["Size"] = AttributeValue(2000.0);
    house3["Bedrooms"] = AttributeValue(4.0);
    house3["Bathrooms"] = AttributeValue(3.0);
    house3["Age"] = AttributeValue(2.0);
    regressionData.emplace_back(house3, 350000.0);
    
    std::unordered_map<std::string, AttributeValue> house4;
    house4["Size"] = AttributeValue(1200.0);
    house4["Bedrooms"] = AttributeValue(2.0);
    house4["Bathrooms"] = AttributeValue(1.0);
    house4["Age"] = AttributeValue(20.0);
    regressionData.emplace_back(house4, 150000.0);
    
    // 训练回归决策树
    DecisionTree regressor(5, 2, true);
    regressor.fit(regressionData, regressionAttrs);
    
    // 打印回归树
    std::cout << "\n===== 回归决策树 ======\n" << std::endl;
    regressor.printTree();
    
    // 测试预测
    std::unordered_map<std::string, AttributeValue> testHouse;
    testHouse["Size"] = AttributeValue(1800.0);
    testHouse["Bedrooms"] = AttributeValue(3.0);
    testHouse["Bathrooms"] = AttributeValue(2.0);
    testHouse["Age"] = AttributeValue(8.0);
    
    try {
        double pricePrediction = regressor.predictRegression(testHouse);
        std::cout << "\n测试房屋价格预测: $" << std::fixed << std::setprecision(2) << pricePrediction << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "预测错误: " << e.what() << std::endl;
    }
    
    // 计算均方误差
    try {
        double mse = regressor.calculateMSE(regressionData);
        std::cout << "均方误差: " << std::fixed << std::setprecision(2) << mse << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "计算均方误差错误: " << e.what() << std::endl;
    }
    
    return 0;
}