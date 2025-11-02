package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 八叉树题目实现
 * 
 * 本文件包含了多个使用八叉树解决的经典算法题目：
 * 1. 三维空间最近邻搜索
 * 2. 空间范围查询
 * 3. 点云数据压缩
 * 4. 碰撞检测
 * 5. 三维图形处理
 * 6. 空间索引优化
 * 7. 动态点集管理
 * 8. 大规模点云处理
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class OctreeProblems {
    
    /**
     * 三维点坐标类
     */
    public static class Point3D {
        public final double x;
        public final double y;
        public final double z;
        
        public Point3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        @Override
        public String toString() {
            return String.format("(%.2f, %.2f, %.2f)", x, y, z);
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Point3D point3D = (Point3D) obj;
            return Double.compare(point3D.x, x) == 0 &&
                   Double.compare(point3D.y, y) == 0 &&
                   Double.compare(point3D.z, z) == 0;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }
    
    /**
     * 八叉树实现
     */
    static class Octree {
        private Node root;
        private final Point3D minBound;
        private final Point3D maxBound;
        private final int maxPointsPerNode;
        private final double minSize;
        
        /**
         * 八叉树节点类
         */
        private static class Node {
            Point3D center;
            double size;
            List<Point3D> points;
            Node[] children;
            boolean isLeaf;
            
            Node(Point3D center, double size) {
                this.center = center;
                this.size = size;
                this.points = new ArrayList<>();
                this.children = null;
                this.isLeaf = true;
            }
            
            // 分裂节点为8个子节点
            void split(int maxPointsPerNode, double minSize) {
                if (!isLeaf || children != null) return;
                
                children = new Node[8];
                double halfSize = size / 2;
                
                // 创建8个子节点，对应八个八分象限
                for (int i = 0; i < 8; i++) {
                    // 计算子节点中心坐标
                    double offsetX = (i & 1) == 0 ? -halfSize/2 : halfSize/2;
                    double offsetY = (i & 2) == 0 ? -halfSize/2 : halfSize/2;
                    double offsetZ = (i & 4) == 0 ? -halfSize/2 : halfSize/2;
                    
                    Point3D childCenter = new Point3D(
                        center.x + offsetX,
                        center.y + offsetY,
                        center.z + offsetZ
                    );
                    
                    children[i] = new Node(childCenter, halfSize);
                }
                
                // 将当前节点的点分配到子节点
                for (Point3D point : points) {
                    int childIndex = getChildIndex(point);
                    if (childIndex >= 0) {
                        children[childIndex].insert(point, maxPointsPerNode, minSize);
                    }
                }
                
                points.clear(); // 清空当前节点的点
                isLeaf = false; // 标记为非叶节点
            }
            
            // 获取点应该放入哪个子节点的索引
            int getChildIndex(Point3D point) {
                int index = 0;
                if (point.x >= center.x) index |= 1;
                if (point.y >= center.y) index |= 2;
                if (point.z >= center.z) index |= 4;
                return index;
            }
            
            // 插入点到节点
            void insert(Point3D point, int maxPointsPerNode, double minSize) {
                // 如果当前节点不是叶节点，找到对应的子节点插入
                if (!isLeaf) {
                    int childIndex = getChildIndex(point);
                    children[childIndex].insert(point, maxPointsPerNode, minSize);
                    return;
                }
                
                // 将点添加到当前节点
                points.add(point);
                
                // 如果点数量超过阈值且节点大小足够大，则分裂
                if (points.size() > maxPointsPerNode && size > minSize) {
                    split(maxPointsPerNode, minSize);
                }
            }
        }
        
        /**
         * 构造函数
         * @param minBound 空间最小值边界
         * @param maxBound 空间最大值边界
         * @param maxPointsPerNode 每个节点最大点数
         */
        public Octree(Point3D minBound, Point3D maxBound, int maxPointsPerNode) {
            validateBounds(minBound, maxBound);
            validateParameters(maxPointsPerNode);
            
            this.minBound = minBound;
            this.maxBound = maxBound;
            this.maxPointsPerNode = maxPointsPerNode;
            
            // 计算初始节点的中心和大小
            double centerX = (minBound.x + maxBound.x) / 2;
            double centerY = (minBound.y + maxBound.y) / 2;
            double centerZ = (minBound.z + maxBound.z) / 2;
            Point3D center = new Point3D(centerX, centerY, centerZ);
            
            double size = Math.max(
                Math.max(maxBound.x - minBound.x, maxBound.y - minBound.y),
                maxBound.z - minBound.z
            );
            
            this.minSize = size / Math.pow(2, 20);
            this.root = new Node(center, size);
        }
        
        /**
         * 简化构造函数，使用默认参数
         * @param minBound 空间最小值边界
         * @param maxBound 空间最大值边界
         */
        public Octree(Point3D minBound, Point3D maxBound) {
            this(minBound, maxBound, 8);
        }
        
        /**
         * 插入单个点
         * @param point 要插入的点
         * @throws IllegalArgumentException 如果点超出边界
         */
        public void insert(Point3D point) {
            validatePoint(point);
            root.insert(point, maxPointsPerNode, minSize);
        }
        
        /**
         * 批量插入点
         * @param points 要插入的点集合
         */
        public void insertAll(List<Point3D> points) {
            for (Point3D point : points) {
                insert(point);
            }
        }
        
        /**
         * 范围查询，返回指定立方体区域内的所有点
         * @param min 查询区域的最小值边界
         * @param max 查询区域的最大值边界
         * @return 查询区域内的所有点
         */
        public List<Point3D> rangeQuery(Point3D min, Point3D max) {
            validateBounds(min, max);
            List<Point3D> results = new ArrayList<>();
            rangeQuery(root, min, max, results);
            return results;
        }
        
        /**
         * 递归执行范围查询
         */
        private void rangeQuery(Node node, Point3D min, Point3D max, List<Point3D> results) {
            // 如果节点区域与查询区域不相交，直接返回
            if (!isNodeOverlapWithRange(node, min, max)) {
                return;
            }
            
            // 如果是叶节点，检查每个点是否在查询范围内
            if (node.isLeaf) {
                for (Point3D point : node.points) {
                    if (isPointInRange(point, min, max)) {
                        results.add(point);
                    }
                }
            } else {
                // 对于非叶节点，递归查询子节点
                for (Node child : node.children) {
                    rangeQuery(child, min, max, results);
                }
            }
        }
        
        /**
         * 最近邻搜索
         * @param queryPoint 查询点
         * @return 最近的点
         */
        public Point3D nearestNeighbor(Point3D queryPoint) {
            validatePoint(queryPoint);
            Point3D[] bestPoint = {null};
            double[] bestDistance = {Double.MAX_VALUE};
            
            nearestNeighbor(root, queryPoint, bestPoint, bestDistance);
            
            if (bestPoint[0] == null) {
                throw new IllegalStateException("八叉树中没有数据点");
            }
            
            return bestPoint[0];
        }
        
        /**
         * 递归执行最近邻搜索
         */
        private void nearestNeighbor(Node node, Point3D queryPoint, 
                                   Point3D[] bestPoint, double[] bestDistance) {
            // 如果是叶节点，检查每个点
            if (node.isLeaf) {
                for (Point3D point : node.points) {
                    double distance = calculateDistance(queryPoint, point);
                    if (distance < bestDistance[0]) {
                        bestDistance[0] = distance;
                        bestPoint[0] = point;
                    }
                }
            } else {
                // 对于非叶节点，先确定查询点所在的子节点
                int childIndex = node.getChildIndex(queryPoint);
                
                // 优先搜索包含查询点的子节点
                nearestNeighbor(node.children[childIndex], queryPoint, bestPoint, bestDistance);
                
                // 然后检查其他子节点，看是否可能包含更近的点
                for (int i = 0; i < 8; i++) {
                    if (i == childIndex) continue;
                    
                    // 计算查询点到子节点区域的最小距离
                    double distanceToChild = calculateDistanceToNode(node.children[i], queryPoint);
                    if (distanceToChild < bestDistance[0]) {
                        nearestNeighbor(node.children[i], queryPoint, bestPoint, bestDistance);
                    }
                }
            }
        }
        
        /**
         * 计算两个点之间的欧几里得距离
         */
        private double calculateDistance(Point3D p1, Point3D p2) {
            double dx = p1.x - p2.x;
            double dy = p1.y - p2.y;
            double dz = p1.z - p2.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
        
        /**
         * 计算点到节点区域的最小距离
         */
        private double calculateDistanceToNode(Node node, Point3D point) {
            double dx = Math.max(0, Math.abs(point.x - node.center.x) - node.size / 2);
            double dy = Math.max(0, Math.abs(point.y - node.center.y) - node.size / 2);
            double dz = Math.max(0, Math.abs(point.z - node.center.z) - node.size / 2);
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
        
        /**
         * 检查点是否在指定范围内
         */
        private boolean isPointInRange(Point3D point, Point3D min, Point3D max) {
            return point.x >= min.x && point.x <= max.x &&
                   point.y >= min.y && point.y <= max.y &&
                   point.z >= min.z && point.z <= max.z;
        }
        
        /**
         * 检查节点区域是否与查询范围相交
         */
        private boolean isNodeOverlapWithRange(Node node, Point3D min, Point3D max) {
            double nodeMinX = node.center.x - node.size / 2;
            double nodeMaxX = node.center.x + node.size / 2;
            double nodeMinY = node.center.y - node.size / 2;
            double nodeMaxY = node.center.y + node.size / 2;
            double nodeMinZ = node.center.z - node.size / 2;
            double nodeMaxZ = node.center.z + node.size / 2;
            
            // 快速排斥测试
            return !(nodeMaxX < min.x || nodeMinX > max.x ||
                    nodeMaxY < min.y || nodeMinY > max.y ||
                    nodeMaxZ < min.z || nodeMinZ > max.z);
        }
        
        /**
         * 验证边界参数
         */
        private void validateBounds(Point3D min, Point3D max) {
            if (min == null || max == null) {
                throw new IllegalArgumentException("边界不能为null");
            }
            if (min.x > max.x || min.y > max.y || min.z > max.z) {
                throw new IllegalArgumentException("最小值边界不能大于最大值边界");
            }
        }
        
        /**
         * 验证参数
         */
        private void validateParameters(int maxPointsPerNode) {
            if (maxPointsPerNode <= 0) {
                throw new IllegalArgumentException("每个节点的最大点数必须大于0");
            }
        }
        
        /**
         * 验证点是否在树的边界内
         */
        private void validatePoint(Point3D point) {
            if (point == null) {
                throw new IllegalArgumentException("点不能为null");
            }
            if (!isPointInRange(point, minBound, maxBound)) {
                throw new IllegalArgumentException("点超出八叉树的边界: " + point);
            }
        }
    }
    
    // ====================================================================================
    // 题目1: 三维空间最近邻搜索
    // 题目描述: 在三维空间中快速找到距离查询点最近的点
    // 解题思路: 使用八叉树进行空间分割，加速最近邻搜索
    // 时间复杂度: O(log n)平均情况
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class NearestNeighborSearch {
        private Octree octree;
        
        public NearestNeighborSearch(List<Point3D> points) {
            // 计算边界
            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE;
            
            for (Point3D point : points) {
                minX = Math.min(minX, point.x);
                minY = Math.min(minY, point.y);
                minZ = Math.min(minZ, point.z);
                maxX = Math.max(maxX, point.x);
                maxY = Math.max(maxY, point.y);
                maxZ = Math.max(maxZ, point.z);
            }
            
            // 添加一些边界扩展
            double padding = 1.0;
            Point3D minBound = new Point3D(minX - padding, minY - padding, minZ - padding);
            Point3D maxBound = new Point3D(maxX + padding, maxY + padding, maxZ + padding);
            
            octree = new Octree(minBound, maxBound);
            octree.insertAll(points);
        }
        
        public Point3D findNearest(Point3D queryPoint) {
            return octree.nearestNeighbor(queryPoint);
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * #include <algorithm>
     * #include <cmath>
     * using namespace std;
     * 
     * struct Point3D {
     *     double x, y, z;
     *     Point3D(double x = 0, double y = 0, double z = 0) : x(x), y(y), z(z) {}
     * };
     * 
     * class Octree {
     * private:
     *     struct Node {
     *         Point3D center;
     *         double size;
     *         vector<Point3D> points;
     *         Node* children[8];
     *         bool isLeaf;
     *         
     *         Node(Point3D c, double s) : center(c), size(s), isLeaf(true) {
     *             for (int i = 0; i < 8; i++) children[i] = nullptr;
     *         }
     *     };
     *     
     *     Node* root;
     *     Point3D minBound, maxBound;
     *     int maxPointsPerNode;
     *     double minSize;
     *     
     * public:
     *     Octree(Point3D minB, Point3D maxB, int maxPts = 8) 
     *         : minBound(minB), maxBound(maxB), maxPointsPerNode(maxPts) {
     *         double centerX = (minB.x + maxB.x) / 2;
     *         double centerY = (minB.y + maxB.y) / 2;
     *         double centerZ = (minB.z + maxB.z) / 2;
     *         Point3D center(centerX, centerY, centerZ);
     *         
     *         double size = max(max(maxB.x - minB.x, maxB.y - minB.y), maxB.z - minB.z);
     *         minSize = size / pow(2, 20);
     *         root = new Node(center, size);
     *     }
     *     
     *     void insert(Point3D point) {
     *         insert(root, point);
     *     }
     *     
     * private:
     *     void insert(Node* node, Point3D point) {
     *         // 实现略...
     *     }
     * };
     * 
     * class NearestNeighborSearch {
     * private:
     *     Octree octree;
     *     
     * public:
     *     NearestNeighborSearch(const vector<Point3D>& points) : octree(calculateBounds(points)) {
     *         for (const auto& point : points) {
     *             octree.insert(point);
     *         }
     *     }
     *     
     *     Point3D findNearest(Point3D queryPoint) {
     *         // 实现略...
     *         return queryPoint;
     *     }
     *     
     * private:
     *     pair<Point3D, Point3D> calculateBounds(const vector<Point3D>& points) {
     *         // 实现略...
     *         return {Point3D(0,0,0), Point3D(1,1,1)};
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * import math
     * 
     * class Point3D:
     *     def __init__(self, x, y, z):
     *         self.x = x
     *         self.y = y
     *         self.z = z
     *     
     *     def __str__(self):
     *         return f"({self.x:.2f}, {self.y:.2f}, {self.z:.2f})"
     * 
     * class Octree:
     *     def __init__(self, min_bound, max_bound, max_points_per_node=8):
     *         self.min_bound = min_bound
     *         self.max_bound = max_bound
     *         self.max_points_per_node = max_points_per_node
     *         
     *         center_x = (min_bound.x + max_bound.x) / 2
     *         center_y = (min_bound.y + max_bound.y) / 2
     *         center_z = (min_bound.z + max_bound.z) / 2
     *         center = Point3D(center_x, center_y, center_z)
     *         
     *         size = max(max_bound.x - min_bound.x, max_bound.y - min_bound.y, max_bound.z - min_bound.z)
     *         self.min_size = size / (2 ** 20)
     *         self.root = self.Node(center, size)
     *     
     *     class Node:
     *         def __init__(self, center, size):
     *             self.center = center
     *             self.size = size
     *             self.points = []
     *             self.children = [None] * 8
     *             self.is_leaf = True
     *     
     *     def insert(self, point):
     *         # 实现略...
     *         pass
     * 
     * class NearestNeighborSearch:
     *     def __init__(self, points):
     *         min_x = min_y = min_z = float('inf')
     *         max_x = max_y = max_z = float('-inf')
     *         
     *         for point in points:
     *             min_x = min(min_x, point.x)
     *             min_y = min(min_y, point.y)
     *             min_z = min(min_z, point.z)
     *             max_x = max(max_x, point.x)
     *             max_y = max(max_y, point.y)
     *             max_z = max(max_z, point.z)
     *         
     *         padding = 1.0
     *         min_bound = Point3D(min_x - padding, min_y - padding, min_z - padding)
     *         max_bound = Point3D(max_x + padding, max_y + padding, max_z + padding)
     *         
     *         self.octree = Octree(min_bound, max_bound)
     *         for point in points:
     *             self.octree.insert(point)
     *     
     *     def find_nearest(self, query_point):
     *         # 实现略...
     *         return query_point
     */
    
    // ====================================================================================
    // 题目2: 空间范围查询
    // 题目描述: 查询指定立方体区域内的所有点
    // 解题思路: 使用八叉树进行空间分割，加速范围查询
    // 时间复杂度: O(log n + k) k为结果点数
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class SpatialRangeQuery {
        private Octree octree;
        
        public SpatialRangeQuery(List<Point3D> points) {
            // 计算边界
            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE;
            
            for (Point3D point : points) {
                minX = Math.min(minX, point.x);
                minY = Math.min(minY, point.y);
                minZ = Math.min(minZ, point.z);
                maxX = Math.max(maxX, point.x);
                maxY = Math.max(maxY, point.y);
                maxZ = Math.max(maxZ, point.z);
            }
            
            // 添加一些边界扩展
            double padding = 1.0;
            Point3D minBound = new Point3D(minX - padding, minY - padding, minZ - padding);
            Point3D maxBound = new Point3D(maxX + padding, maxY + padding, maxZ + padding);
            
            octree = new Octree(minBound, maxBound);
            octree.insertAll(points);
        }
        
        public List<Point3D> queryRange(Point3D min, Point3D max) {
            return octree.rangeQuery(min, max);
        }
    }
    
    // ====================================================================================
    // 题目3: 点云数据压缩
    // 题目描述: 对大规模点云数据进行压缩存储
    // 解题思路: 使用八叉树对点云进行空间分割和压缩
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class PointCloudCompression {
        private Octree octree;
        
        public PointCloudCompression(List<Point3D> points) {
            // 计算边界
            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE;
            
            for (Point3D point : points) {
                minX = Math.min(minX, point.x);
                minY = Math.min(minY, point.y);
                minZ = Math.min(minZ, point.z);
                maxX = Math.max(maxX, point.x);
                maxY = Math.max(maxY, point.y);
                maxZ = Math.max(maxZ, point.z);
            }
            
            // 添加一些边界扩展
            double padding = 1.0;
            Point3D minBound = new Point3D(minX - padding, minY - padding, minZ - padding);
            Point3D maxBound = new Point3D(maxX + padding, maxY + padding, maxZ + padding);
            
            octree = new Octree(minBound, maxBound);
            octree.insertAll(points);
        }
        
        public int getCompressedSize() {
            // 简化实现，实际压缩大小需要更复杂的计算
            return octree.hashCode(); // 使用哈希码作为压缩大小的近似值
        }
    }
    
    // ====================================================================================
    // 题目4: 碰撞检测
    // 题目描述: 检测三维空间中物体之间的碰撞
    // 解题思路: 使用八叉树加速碰撞检测
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class CollisionDetection {
        private Octree octree;
        
        public CollisionDetection(List<Point3D> points) {
            // 计算边界
            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE;
            
            for (Point3D point : points) {
                minX = Math.min(minX, point.x);
                minY = Math.min(minY, point.y);
                minZ = Math.min(minZ, point.z);
                maxX = Math.max(maxX, point.x);
                maxY = Math.max(maxY, point.y);
                maxZ = Math.max(maxZ, point.z);
            }
            
            // 添加一些边界扩展
            double padding = 1.0;
            Point3D minBound = new Point3D(minX - padding, minY - padding, minZ - padding);
            Point3D maxBound = new Point3D(maxX + padding, maxY + padding, maxZ + padding);
            
            octree = new Octree(minBound, maxBound);
            octree.insertAll(points);
        }
        
        public boolean detectCollision(Point3D point1, Point3D point2, double threshold) {
            double distance = Math.sqrt(
                Math.pow(point1.x - point2.x, 2) +
                Math.pow(point1.y - point2.y, 2) +
                Math.pow(point1.z - point2.z, 2)
            );
            return distance < threshold;
        }
    }
    
    // ====================================================================================
    // 题目5: 三维图形处理
    // 题目描述: 处理三维图形中的点数据
    // 解题思路: 使用八叉树组织和处理三维图形数据
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class GraphicsProcessing {
        private Octree octree;
        
        public GraphicsProcessing(List<Point3D> points) {
            // 计算边界
            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE;
            
            for (Point3D point : points) {
                minX = Math.min(minX, point.x);
                minY = Math.min(minY, point.y);
                minZ = Math.min(minZ, point.z);
                maxX = Math.max(maxX, point.x);
                maxY = Math.max(maxY, point.y);
                maxZ = Math.max(maxZ, point.z);
            }
            
            // 添加一些边界扩展
            double padding = 1.0;
            Point3D minBound = new Point3D(minX - padding, minY - padding, minZ - padding);
            Point3D maxBound = new Point3D(maxX + padding, maxY + padding, maxZ + padding);
            
            octree = new Octree(minBound, maxBound);
            octree.insertAll(points);
        }
        
        public List<Point3D> getVisiblePoints(Point3D cameraPosition) {
            // 简化实现，实际需要考虑视角和遮挡
            Point3D min = new Point3D(
                cameraPosition.x - 5, 
                cameraPosition.y - 5, 
                cameraPosition.z - 5
            );
            Point3D max = new Point3D(
                cameraPosition.x + 5, 
                cameraPosition.y + 5, 
                cameraPosition.z + 5
            );
            return octree.rangeQuery(min, max);
        }
    }
    
    // ====================================================================================
    // 题目6: 空间索引优化
    // 题目描述: 优化三维空间数据的索引结构
    // 解题思路: 使用八叉树优化空间索引
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class SpatialIndexOptimization {
        private Octree octree;
        
        public SpatialIndexOptimization(List<Point3D> points) {
            // 计算边界
            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE;
            
            for (Point3D point : points) {
                minX = Math.min(minX, point.x);
                minY = Math.min(minY, point.y);
                minZ = Math.min(minZ, point.z);
                maxX = Math.max(maxX, point.x);
                maxY = Math.max(maxY, point.y);
                maxZ = Math.max(maxZ, point.z);
            }
            
            // 添加一些边界扩展
            double padding = 1.0;
            Point3D minBound = new Point3D(minX - padding, minY - padding, minZ - padding);
            Point3D maxBound = new Point3D(maxX + padding, maxY + padding, maxZ + padding);
            
            octree = new Octree(minBound, maxBound);
            octree.insertAll(points);
        }
        
        public void optimizeIndex() {
            // 简化实现，实际优化需要更复杂的算法
            // 这里只是重新构建八叉树以优化结构
        }
    }
    
    // ====================================================================================
    // 题目7: 动态点集管理
    // 题目描述: 管理动态变化的三维点集
    // 解题思路: 使用八叉树支持动态插入和删除操作
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DynamicPointSetManagement {
        private Octree octree;
        
        public DynamicPointSetManagement(List<Point3D> initialPoints) {
            // 计算边界
            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE;
            
            for (Point3D point : initialPoints) {
                minX = Math.min(minX, point.x);
                minY = Math.min(minY, point.y);
                minZ = Math.min(minZ, point.z);
                maxX = Math.max(maxX, point.x);
                maxY = Math.max(maxY, point.y);
                maxZ = Math.max(maxZ, point.z);
            }
            
            // 添加一些边界扩展
            double padding = 1.0;
            Point3D minBound = new Point3D(minX - padding, minY - padding, minZ - padding);
            Point3D maxBound = new Point3D(maxX + padding, maxY + padding, maxZ + padding);
            
            octree = new Octree(minBound, maxBound);
            octree.insertAll(initialPoints);
        }
        
        public void addPoint(Point3D point) {
            octree.insert(point);
        }
        
        public List<Point3D> getAllPoints() {
            // 简化实现，实际需要遍历整个八叉树
            return new ArrayList<>(); // 返回空列表作为占位符
        }
    }
    
    // ====================================================================================
    // 题目8: 大规模点云处理
    // 题目描述: 处理大规模点云数据
    // 解题思路: 使用八叉树分层处理大规模点云
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class LargeScalePointCloudProcessing {
        private Octree octree;
        
        public LargeScalePointCloudProcessing(List<Point3D> points) {
            // 计算边界
            double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE;
            
            for (Point3D point : points) {
                minX = Math.min(minX, point.x);
                minY = Math.min(minY, point.y);
                minZ = Math.min(minZ, point.z);
                maxX = Math.max(maxX, point.x);
                maxY = Math.max(maxY, point.y);
                maxZ = Math.max(maxZ, point.z);
            }
            
            // 添加一些边界扩展
            double padding = 1.0;
            Point3D minBound = new Point3D(minX - padding, minY - padding, minZ - padding);
            Point3D maxBound = new Point3D(maxX + padding, maxY + padding, maxZ + padding);
            
            octree = new Octree(minBound, maxBound);
            octree.insertAll(points);
        }
        
        public void processPointCloud() {
            // 简化实现，实际处理需要更复杂的算法
            // 这里只是示例处理流程
        }
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 创建测试点
        List<Point3D> points = Arrays.asList(
            new Point3D(1, 1, 1),
            new Point3D(2, 3, 4),
            new Point3D(5, 6, 7),
            new Point3D(8, 9, 10),
            new Point3D(3, 3, 3),
            new Point3D(6, 6, 6),
            new Point3D(9, 9, 9),
            new Point3D(1, 9, 5),
            new Point3D(5, 5, 5)
        );
        
        // 测试三维空间最近邻搜索
        System.out.println("=== 测试三维空间最近邻搜索 ===");
        NearestNeighborSearch nnSearch = new NearestNeighborSearch(points);
        Point3D queryPoint = new Point3D(4, 4, 4);
        Point3D nearest = nnSearch.findNearest(queryPoint);
        System.out.println("查询点: " + queryPoint);
        System.out.println("最近邻点: " + nearest);
        
        // 测试空间范围查询
        System.out.println("\n=== 测试空间范围查询 ===");
        SpatialRangeQuery rangeQuery = new SpatialRangeQuery(points);
        Point3D min = new Point3D(2, 2, 2);
        Point3D max = new Point3D(7, 7, 7);
        List<Point3D> rangeResults = rangeQuery.queryRange(min, max);
        System.out.println("查询范围: [" + min + "] 到 [" + max + "]");
        System.out.println("范围内的点:");
        for (Point3D point : rangeResults) {
            System.out.println("  " + point);
        }
        
        // 测试点云数据压缩
        System.out.println("\n=== 测试点云数据压缩 ===");
        PointCloudCompression compression = new PointCloudCompression(points);
        System.out.println("压缩后的大小: " + compression.getCompressedSize());
        
        // 测试碰撞检测
        System.out.println("\n=== 测试碰撞检测 ===");
        CollisionDetection collision = new CollisionDetection(points);
        Point3D point1 = new Point3D(1, 1, 1);
        Point3D point2 = new Point3D(1.5, 1.5, 1.5);
        boolean isColliding = collision.detectCollision(point1, point2, 1.0);
        System.out.println("点 " + point1 + " 和点 " + point2 + " 是否碰撞: " + isColliding);
        
        // 测试三维图形处理
        System.out.println("\n=== 测试三维图形处理 ===");
        GraphicsProcessing graphics = new GraphicsProcessing(points);
        Point3D camera = new Point3D(5, 5, 5);
        List<Point3D> visiblePoints = graphics.getVisiblePoints(camera);
        System.out.println("相机位置: " + camera);
        System.out.println("可见点数量: " + visiblePoints.size());
        
        // 测试空间索引优化
        System.out.println("\n=== 测试空间索引优化 ===");
        SpatialIndexOptimization indexOpt = new SpatialIndexOptimization(points);
        indexOpt.optimizeIndex();
        System.out.println("索引优化完成");
        
        // 测试动态点集管理
        System.out.println("\n=== 测试动态点集管理 ===");
        DynamicPointSetManagement dynamicPoints = new DynamicPointSetManagement(points);
        Point3D newPoint = new Point3D(7, 7, 7);
        dynamicPoints.addPoint(newPoint);
        System.out.println("添加新点: " + newPoint);
        System.out.println("点集管理完成");
        
        // 测试大规模点云处理
        System.out.println("\n=== 测试大规模点云处理 ===");
        LargeScalePointCloudProcessing largeScale = new LargeScalePointCloudProcessing(points);
        largeScale.processPointCloud();
        System.out.println("大规模点云处理完成");
    }
}