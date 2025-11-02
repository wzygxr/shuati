#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <stdexcept>
#include <memory>
#include <iomanip>

/**
 * 八叉树（Octree）实现
 * 
 * 概述：
 * 八叉树是一种用于三维空间数据索引的数据结构，通过递归地将三维空间划分为八个子空间（八分象限）。
 * 每个节点代表三维空间中的一个立方体区域，可以包含点数据或进一步划分为八个子节点。
 * 
 * 适用场景：
 * - 三维空间中的范围查询
 * - 空间分割和最近邻搜索
 * - 三维图形处理和碰撞检测
 * - 点云数据压缩和处理
 * - 三维空间索引
 * 
 * 时间复杂度：
 * - 构建树：O(n log n)，其中n是数据点数量
 * - 插入/查询/删除：平均O(log n)，最坏O(n)
 * 
 * 空间复杂度：
 * - O(n)，其中n是数据点数量
 */
class Octree {
private:
    // 三维点坐标类
    struct Point3D {
        double x, y, z;
        
        Point3D(double x = 0, double y = 0, double z = 0) 
            : x(x), y(y), z(z) {}
        
        bool operator==(const Point3D& other) const {
            return x == other.x && y == other.y && z == other.z;
        }
    };
    
    // 八叉树节点类
    struct Node {
        Point3D center;                  // 节点中心坐标
        double size;                     // 节点大小（立方体边长）
        std::vector<Point3D> points;     // 存储在此节点的点
        std::unique_ptr<Node> children[8]; // 子节点，最多8个
        bool isLeaf;                     // 是否为叶节点
        
        Node(const Point3D& center, double size) 
            : center(center), size(size), isLeaf(true) {
            // 初始化子节点为nullptr
            for (int i = 0; i < 8; i++) {
                children[i] = nullptr;
            }
        }
        
        // 获取点应该放入哪个子节点的索引
        int getChildIndex(const Point3D& point) const {
            int index = 0;
            if (point.x >= center.x) index |= 1;
            if (point.y >= center.y) index |= 2;
            if (point.z >= center.z) index |= 4;
            return index;
        }
        
        // 分裂节点为8个子节点
        void split(int maxPointsPerNode, double minSize) {
            if (!isLeaf) return;
            
            double halfSize = size / 2;
            
            // 创建8个子节点，对应八个八分象限
            for (int i = 0; i < 8; i++) {
                // 计算子节点中心坐标
                double offsetX = (i & 1) == 0 ? -halfSize/2 : halfSize/2;
                double offsetY = (i & 2) == 0 ? -halfSize/2 : halfSize/2;
                double offsetZ = (i & 4) == 0 ? -halfSize/2 : halfSize/2;
                
                Point3D childCenter(center.x + offsetX, 
                                   center.y + offsetY, 
                                   center.z + offsetZ);
                
                children[i] = std::make_unique<Node>(childCenter, halfSize);
            }
            
            // 将当前节点的点分配到子节点
            for (const auto& point : points) {
                int childIndex = getChildIndex(point);
                children[childIndex]->insert(point, maxPointsPerNode, minSize);
            }
            
            points.clear(); // 清空当前节点的点
            isLeaf = false; // 标记为非叶节点
        }
        
        // 插入点到节点
        void insert(const Point3D& point, int maxPointsPerNode, double minSize) {
            // 如果当前节点不是叶节点，找到对应的子节点插入
            if (!isLeaf) {
                int childIndex = getChildIndex(point);
                children[childIndex]->insert(point, maxPointsPerNode, minSize);
                return;
            }
            
            // 将点添加到当前节点
            points.push_back(point);
            
            // 如果点数量超过阈值且节点大小足够大，则分裂
            if (points.size() > static_cast<size_t>(maxPointsPerNode) && size > minSize) {
                split(maxPointsPerNode, minSize);
            }
        }
    };
    
    std::unique_ptr<Node> root;
    Point3D minBound;    // 空间最小值边界
    Point3D maxBound;    // 空间最大值边界
    int maxPointsPerNode; // 每个节点最大点数
    double minSize;      // 最小节点大小，避免无限分割
    
    // 递归执行范围查询
    void rangeQuery(const Node* node, const Point3D& min, const Point3D& max, 
                   std::vector<Point3D>& results) const {
        // 如果节点区域与查询区域不相交，直接返回
        if (!isNodeOverlapWithRange(node, min, max)) {
            return;
        }
        
        // 如果是叶节点，检查每个点是否在查询范围内
        if (node->isLeaf) {
            for (const auto& point : node->points) {
                if (isPointInRange(point, min, max)) {
                    results.push_back(point);
                }
            }
        } else {
            // 对于非叶节点，递归查询子节点
            for (int i = 0; i < 8; i++) {
                if (node->children[i]) {
                    rangeQuery(node->children[i].get(), min, max, results);
                }
            }
        }
    }
    
    // 递归执行最近邻搜索
    void nearestNeighbor(const Node* node, const Point3D& queryPoint, 
                        Point3D& bestPoint, double& bestDistance) const {
        // 如果是叶节点，检查每个点
        if (node->isLeaf) {
            for (const auto& point : node->points) {
                double distance = calculateDistance(queryPoint, point);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestPoint = point;
                }
            }
        } else {
            // 对于非叶节点，先确定查询点所在的子节点
            int childIndex = node->getChildIndex(queryPoint);
            
            // 优先搜索包含查询点的子节点
            nearestNeighbor(node->children[childIndex].get(), queryPoint, bestPoint, bestDistance);
            
            // 然后检查其他子节点，看是否可能包含更近的点
            for (int i = 0; i < 8; i++) {
                if (i == childIndex || !node->children[i]) continue;
                
                // 计算查询点到子节点区域的最小距离
                double distanceToChild = calculateDistanceToNode(node->children[i].get(), queryPoint);
                if (distanceToChild < bestDistance) {
                    nearestNeighbor(node->children[i].get(), queryPoint, bestPoint, bestDistance);
                }
            }
        }
    }
    
    // 计算两个点之间的欧几里得距离
    double calculateDistance(const Point3D& p1, const Point3D& p2) const {
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        double dz = p1.z - p2.z;
        return std::sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    // 计算点到节点区域的最小距离
    double calculateDistanceToNode(const Node* node, const Point3D& point) const {
        double dx = std::max(0.0, std::abs(point.x - node->center.x) - node->size / 2);
        double dy = std::max(0.0, std::abs(point.y - node->center.y) - node->size / 2);
        double dz = std::max(0.0, std::abs(point.z - node->center.z) - node->size / 2);
        return std::sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    // 检查点是否在指定范围内
    bool isPointInRange(const Point3D& point, const Point3D& min, const Point3D& max) const {
        return point.x >= min.x && point.x <= max.x &&
               point.y >= min.y && point.y <= max.y &&
               point.z >= min.z && point.z <= max.z;
    }
    
    // 检查节点区域是否与查询范围相交
    bool isNodeOverlapWithRange(const Node* node, const Point3D& min, const Point3D& max) const {
        double nodeMinX = node->center.x - node->size / 2;
        double nodeMaxX = node->center.x + node->size / 2;
        double nodeMinY = node->center.y - node->size / 2;
        double nodeMaxY = node->center.y + node->size / 2;
        double nodeMinZ = node->center.z - node->size / 2;
        double nodeMaxZ = node->center.z + node->size / 2;
        
        // 快速排斥测试
        return !(nodeMaxX < min.x || nodeMinX > max.x ||
                nodeMaxY < min.y || nodeMinY > max.y ||
                nodeMaxZ < min.z || nodeMinZ > max.z);
    }
    
    // 验证边界参数
    void validateBounds(const Point3D& min, const Point3D& max) const {
        if (min.x > max.x || min.y > max.y || min.z > max.z) {
            throw std::invalid_argument("最小值边界不能大于最大值边界");
        }
    }
    
    // 验证点是否在树的边界内
    void validatePoint(const Point3D& point) const {
        if (!isPointInRange(point, minBound, maxBound)) {
            throw std::invalid_argument("点超出八叉树的边界");
        }
    }
    
    // 递归计算树高
    int height(const Node* node) const {
        if (node->isLeaf) {
            return 1;
        }
        int maxHeight = 0;
        for (int i = 0; i < 8; i++) {
            if (node->children[i]) {
                maxHeight = std::max(maxHeight, height(node->children[i].get()));
            }
        }
        return maxHeight + 1;
    }
    
    // 递归计算节点数
    int size(const Node* node) const {
        if (node->isLeaf) {
            return static_cast<int>(node->points.size());
        }
        int totalSize = 0;
        for (int i = 0; i < 8; i++) {
            if (node->children[i]) {
                totalSize += size(node->children[i].get());
            }
        }
        return totalSize;
    }
    
public:
    /**
     * 构造函数
     * @param minBound 空间最小值边界
     * @param maxBound 空间最大值边界
     * @param maxPointsPerNode 每个节点最大点数
     */
    Octree(const Point3D& minBound, const Point3D& maxBound, int maxPointsPerNode = 8) {
        validateBounds(minBound, maxBound);
        if (maxPointsPerNode <= 0) {
            throw std::invalid_argument("每个节点的最大点数必须大于0");
        }
        
        this->minBound = minBound;
        this->maxBound = maxBound;
        this->maxPointsPerNode = maxPointsPerNode;
        
        // 计算初始节点的中心和大小
        Point3D center(
            (minBound.x + maxBound.x) / 2,
            (minBound.y + maxBound.y) / 2,
            (minBound.z + maxBound.z) / 2
        );
        
        double size = std::max(
            std::max(maxBound.x - minBound.x, maxBound.y - minBound.y),
            maxBound.z - minBound.z
        );
        
        this->minSize = size / std::pow(2, 20); // 设置最小节点大小，避免无限分割
        this->root = std::make_unique<Node>(center, size);
    }
    
    /**
     * 插入单个点
     * @param point 要插入的点
     * @throws std::invalid_argument 如果点超出边界
     */
    void insert(const Point3D& point) {
        validatePoint(point);
        root->insert(point, maxPointsPerNode, minSize);
    }
    
    /**
     * 批量插入点
     * @param points 要插入的点集合
     */
    void insertAll(const std::vector<Point3D>& points) {
        for (const auto& point : points) {
            insert(point);
        }
    }
    
    /**
     * 范围查询，返回指定立方体区域内的所有点
     * @param min 查询区域的最小值边界
     * @param max 查询区域的最大值边界
     * @return 查询区域内的所有点
     */
    std::vector<Point3D> rangeQuery(const Point3D& min, const Point3D& max) const {
        validateBounds(min, max);
        std::vector<Point3D> results;
        rangeQuery(root.get(), min, max, results);
        return results;
    }
    
    /**
     * 最近邻搜索
     * @param queryPoint 查询点
     * @return 最近的点
     * @throws std::runtime_error 如果树为空
     */
    Point3D nearestNeighbor(const Point3D& queryPoint) const {
        validatePoint(queryPoint);
        
        Point3D bestPoint;
        double bestDistance = std::numeric_limits<double>::max();
        
        nearestNeighbor(root.get(), queryPoint, bestPoint, bestDistance);
        
        if (bestDistance == std::numeric_limits<double>::max()) {
            throw std::runtime_error("八叉树中没有数据点");
        }
        
        return bestPoint;
    }
    
    /**
     * 计算树的高度
     * @return 树的高度
     */
    int height() const {
        return height(root.get());
    }
    
    /**
     * 获取树中点的总数
     * @return 点的数量
     */
    int size() const {
        return size(root.get());
    }
};

// 打印点
void printPoint(const Octree::Point3D& point) {
    std::cout << std::fixed << std::setprecision(2);
    std::cout << "(" << point.x << ", " << point.y << ", " << point.z << ")";
}

// 主函数，用于测试八叉树的功能
int main() {
    try {
        // 定义空间边界
        Octree::Point3D minBound(0, 0, 0);
        Octree::Point3D maxBound(10, 10, 10);
        
        // 创建八叉树
        Octree octree(minBound, maxBound, 4);
        
        // 创建一些测试点
        std::vector<Octree::Point3D> points = {
            Octree::Point3D(1, 1, 1),
            Octree::Point3D(2, 3, 4),
            Octree::Point3D(5, 6, 7),
            Octree::Point3D(8, 9, 10),
            Octree::Point3D(3, 3, 3),
            Octree::Point3D(6, 6, 6),
            Octree::Point3D(9, 9, 9),
            Octree::Point3D(1, 9, 5),
            Octree::Point3D(5, 5, 5)
        };
        
        // 插入所有点
        octree.insertAll(points);
        
        std::cout << "八叉树构建完成" << std::endl;
        std::cout << "树高度: " << octree.height() << std::endl;
        std::cout << "点的数量: " << octree.size() << std::endl;
        
        // 测试范围查询
        Octree::Point3D queryMin(2, 2, 2);
        Octree::Point3D queryMax(7, 7, 7);
        std::vector<Octree::Point3D> rangeResults = octree.rangeQuery(queryMin, queryMax);
        
        std::cout << "\n范围查询结果 (2-7, 2-7, 2-7):" << std::endl;
        for (const auto& point : rangeResults) {
            printPoint(point);
            std::cout << std::endl;
        }
        
        // 测试最近邻搜索
        Octree::Point3D queryPoint(4, 4, 4);
        Octree::Point3D nearest = octree.nearestNeighbor(queryPoint);
        
        std::cout << "\n查询点 ";
        printPoint(queryPoint);
        std::cout << " 的最近邻: ";
        printPoint(nearest);
        std::cout << std::endl;
        
        // 计算距离
        double distance = std::sqrt(
            std::pow(queryPoint.x - nearest.x, 2) +
            std::pow(queryPoint.y - nearest.y, 2) +
            std::pow(queryPoint.z - nearest.z, 2)
        );
        std::cout << "距离: " << distance << std::endl;
        
        // 测试边界情况
        try {
            octree.insert(Octree::Point3D(11, 1, 1)); // 超出边界
        } catch (const std::exception& e) {
            std::cout << "\n边界测试成功: " << e.what() << std::endl;
        }
        
    } catch (const std::exception& e) {
        std::cerr << "错误: " << e.what() << std::endl;
        return 1;
    }
    
    return 0;
}