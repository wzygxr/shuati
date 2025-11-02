#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <stdexcept>
#include <random>

/**
 * K-D树（K-Dimensional Tree）实现
 * 
 * 概述：
 * K-D树是一种用于高维空间数据索引的数据结构，特别适合范围查询和最近邻搜索。
 * 它通过递归地将空间划分为垂直于坐标轴的超平面，将数据集组织成树形结构。
 * 
 * 适用场景：
 * - 高维空间中的范围查询
 * - 最近邻搜索
 * - 计算机图形学中的碰撞检测
 * - 机器学习中的快速聚类
 * 
 * 时间复杂度：
 * - 构建树：O(n log n)，其中n是数据点数量
 * - 范围查询：平均O(n^(1-1/k))，最坏O(n)，其中k是维度
 * - 最近邻搜索：平均O(log n)，最坏O(n)
 * 
 * 空间复杂度：
 * - O(n)，其中n是数据点数量
 */
class KdTree {
private:
    struct Node {
        std::vector<double> point; // 数据点坐标
        Node* left;                // 左子树
        Node* right;               // 右子树
        int splitDim;              // 分割维度
        
        Node(const std::vector<double>& p, int dim) 
            : point(p), left(nullptr), right(nullptr), splitDim(dim) {}
        
        ~Node() {
            delete left;
            delete right;
        }
    };
    
    Node* root;
    int k; // 维度
    std::mt19937 rng; // 随机数生成器
    
    // 递归构建K-D树
    Node* buildTree(std::vector<std::vector<double>>& points, int depth) {
        if (points.empty()) {
            return nullptr;
        }
        
        // 根据深度选择分割维度
        int splitDim = depth % k;
        
        // 根据分割维度对点进行排序
        std::sort(points.begin(), points.end(), 
                 [splitDim](const std::vector<double>& a, const std::vector<double>& b) {
                     return a[splitDim] < b[splitDim];
                 });
        
        // 选择中间点作为根节点
        size_t medianIndex = points.size() / 2;
        Node* node = new Node(points[medianIndex], splitDim);
        
        // 准备左右子树的数据点
        std::vector<std::vector<double>> leftPoints(points.begin(), points.begin() + medianIndex);
        std::vector<std::vector<double>> rightPoints(points.begin() + medianIndex + 1, points.end());
        
        // 递归构建左右子树
        node->left = buildTree(leftPoints, depth + 1);
        node->right = buildTree(rightPoints, depth + 1);
        
        return node;
    }
    
    // 递归执行范围查询
    void rangeQuery(Node* node, const std::vector<double>& lowerBound, 
                   const std::vector<double>& upperBound, 
                   std::vector<std::vector<double>>& result) {
        if (node == nullptr) {
            return;
        }
        
        // 检查当前点是否在范围内
        bool inRange = true;
        for (int i = 0; i < k; i++) {
            if (node->point[i] < lowerBound[i] || node->point[i] > upperBound[i]) {
                inRange = false;
                break;
            }
        }
        
        if (inRange) {
            result.push_back(node->point);
        }
        
        // 根据分割维度决定是否需要搜索左右子树
        int splitDim = node->splitDim;
        if (node->point[splitDim] >= lowerBound[splitDim]) {
            rangeQuery(node->left, lowerBound, upperBound, result);
        }
        if (node->point[splitDim] <= upperBound[splitDim]) {
            rangeQuery(node->right, lowerBound, upperBound, result);
        }
    }
    
    // 递归执行最近邻搜索
    double nearestNeighbor(Node* node, const std::vector<double>& queryPoint, 
                          double bestDistance, std::vector<double>& bestPoint) {
        if (node == nullptr) {
            return bestDistance;
        }
        
        // 计算当前点与查询点的距离
        double currentDistance = distance(queryPoint, node->point);
        if (currentDistance < bestDistance) {
            bestDistance = currentDistance;
            bestPoint = node->point;
        }
        
        int splitDim = node->splitDim;
        Node* firstChild = nullptr;
        Node* secondChild = nullptr;
        
        // 确定优先搜索的子树
        if (queryPoint[splitDim] < node->point[splitDim]) {
            firstChild = node->left;
            secondChild = node->right;
        } else {
            firstChild = node->right;
            secondChild = node->left;
        }
        
        // 优先搜索更可能包含最近点的子树
        bestDistance = nearestNeighbor(firstChild, queryPoint, bestDistance, bestPoint);
        
        // 判断是否需要搜索另一个子树
        double planeDistance = std::abs(queryPoint[splitDim] - node->point[splitDim]);
        if (planeDistance < bestDistance) {
            bestDistance = nearestNeighbor(secondChild, queryPoint, bestDistance, bestPoint);
        }
        
        return bestDistance;
    }
    
    // 计算两个点之间的欧几里得距离
    double distance(const std::vector<double>& p1, const std::vector<double>& p2) {
        double sum = 0.0;
        for (int i = 0; i < k; i++) {
            double diff = p1[i] - p2[i];
            sum += diff * diff;
        }
        return std::sqrt(sum);
    }
    
    // 验证范围参数
    void validateRange(const std::vector<double>& lowerBound, const std::vector<double>& upperBound) {
        if (lowerBound.size() != k || upperBound.size() != k) {
            throw std::invalid_argument("范围参数维度必须为：" + std::to_string(k));
        }
        for (int i = 0; i < k; i++) {
            if (lowerBound[i] > upperBound[i]) {
                throw std::invalid_argument("下界不能大于上界");
            }
        }
    }
    
    // 验证点参数
    void validatePoint(const std::vector<double>& point) {
        if (point.size() != k) {
            throw std::invalid_argument("点的维度必须为：" + std::to_string(k));
        }
    }
    
    // 递归计算树高
    int height(Node* node) {
        if (node == nullptr) {
            return 0;
        }
        return std::max(height(node->left), height(node->right)) + 1;
    }
    
    // 递归计算节点数
    int size(Node* node) {
        if (node == nullptr) {
            return 0;
        }
        return size(node->left) + size(node->right) + 1;
    }
    
public:
    /**
     * 构造函数
     * @param k 空间维度
     */
    KdTree(int k) : k(k), root(nullptr), rng(std::random_device{}()) {
        if (k <= 0) {
            throw std::invalid_argument("维度必须为正数");
        }
    }
    
    /**
     * 析构函数
     */
    ~KdTree() {
        delete root;
    }
    
    /**
     * 构建K-D树
     * @param points 数据点数组
     */
    void build(const std::vector<std::vector<double>>& points) {
        if (points.empty()) {
            return;
        }
        
        // 验证所有点的维度是否一致
        for (const auto& point : points) {
            if (point.size() != k) {
                throw std::invalid_argument("所有点必须具有相同的维度：" + std::to_string(k));
            }
        }
        
        // 创建副本以避免修改原始数据
        std::vector<std::vector<double>> pointsCopy = points;
        root = buildTree(pointsCopy, 0);
    }
    
    /**
     * 范围查询
     * @param lowerBound 下界
     * @param upperBound 上界
     * @return 范围内的所有点
     */
    std::vector<std::vector<double>> rangeQuery(const std::vector<double>& lowerBound, 
                                              const std::vector<double>& upperBound) {
        validateRange(lowerBound, upperBound);
        std::vector<std::vector<double>> result;
        rangeQuery(root, lowerBound, upperBound, result);
        return result;
    }
    
    /**
     * 最近邻搜索
     * @param queryPoint 查询点
     * @return 最近的点
     */
    std::vector<double> nearestNeighbor(const std::vector<double>& queryPoint) {
        validatePoint(queryPoint);
        if (root == nullptr) {
            throw std::runtime_error("K-D树为空");
        }
        
        std::vector<double> bestPoint = root->point;
        double bestDistance = distance(queryPoint, root->point);
        
        nearestNeighbor(root, queryPoint, bestDistance, bestPoint);
        
        return bestPoint;
    }
    
    /**
     * 获取树的高度
     * @return 树的高度
     */
    int height() {
        return height(root);
    }
    
    /**
     * 获取树中的节点数
     * @return 节点数
     */
    int size() {
        return size(root);
    }
};

/**
 * 打印点
 */
void printPoint(const std::vector<double>& point) {
    std::cout << "[";
    for (size_t i = 0; i < point.size(); i++) {
        std::cout << point[i];
        if (i < point.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]";
}

/**
 * 主函数，用于测试K-D树的功能
 */
int main() {
    try {
        // 创建一个2维的K-D树
        KdTree kdTree(2);
        
        // 构建数据点
        std::vector<std::vector<double>> points = {
            {2.0, 3.0},
            {5.0, 4.0},
            {9.0, 6.0},
            {4.0, 7.0},
            {8.0, 1.0},
            {7.0, 2.0}
        };
        
        // 构建K-D树
        kdTree.build(points);
        
        std::cout << "K-D树构建完成，高度：" << kdTree.height() << std::endl;
        std::cout << "节点数量：" << kdTree.size() << std::endl;
        
        // 测试范围查询
        std::vector<double> lowerBound = {3.0, 2.0};
        std::vector<double> upperBound = {8.0, 6.0};
        std::vector<std::vector<double>> rangeResult = kdTree.rangeQuery(lowerBound, upperBound);
        std::cout << "\n范围查询结果 [(3,2) 到 (8,6)]:" << std::endl;
        for (const auto& point : rangeResult) {
            printPoint(point);
            std::cout << std::endl;
        }
        
        // 测试最近邻搜索
        std::vector<double> queryPoint = {3.5, 4.5};
        std::vector<double> nearest = kdTree.nearestNeighbor(queryPoint);
        std::cout << "\n查询点 ";
        printPoint(queryPoint);
        std::cout << " 的最近邻：";
        printPoint(nearest);
        std::cout << std::endl;
        
        // 边界情况测试
        try {
            std::vector<double> invalidPoint = {1.0}; // 维度不匹配
            kdTree.nearestNeighbor(invalidPoint);
        } catch (const std::exception& e) {
            std::cout << "\n异常测试成功：" << e.what() << std::endl;
        }
        
    } catch (const std::exception& e) {
        std::cerr << "错误：" << e.what() << std::endl;
        return 1;
    }
    
    return 0;
}