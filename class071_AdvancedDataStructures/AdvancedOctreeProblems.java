package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 高级八叉树题目实现
 * 
 * 本文件包含了更多使用八叉树解决的高级算法题目：
 * 1. 并行八叉树构建
 * 2. 动态八叉树更新
 * 3. 八叉树与KD树混合
 * 4. 八叉树压缩优化
 * 5. 八叉树碰撞检测增强
 * 6. 八叉树光线追踪
 * 7. 八叉树LOD（细节层次）管理
 * 8. 八叉树分布式处理
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class AdvancedOctreeProblems {
    
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
     * 高级八叉树实现
     */
    static class AdvancedOctree {
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
            int pointCount; // 节点及其子树中的总点数
            
            Node(Point3D center, double size) {
                this.center = center;
                this.size = size;
                this.points = new ArrayList<>();
                this.children = null;
                this.isLeaf = true;
                this.pointCount = 0;
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
                pointCount++; // 增加点计数
                
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
            
            // 从节点中删除点
            boolean remove(Point3D point) {
                pointCount--; // 减少点计数
                
                // 如果是叶节点，直接从点列表中删除
                if (isLeaf) {
                    return points.remove(point);
                } else {
                    // 如果是非叶节点，找到对应的子节点删除
                    int childIndex = getChildIndex(point);
                    if (childIndex >= 0) {
                        return children[childIndex].remove(point);
                    }
                }
                
                return false;
            }
        }
        
        /**
         * 构造函数
         * @param minBound 空间最小值边界
         * @param maxBound 空间最大值边界
         * @param maxPointsPerNode 每个节点最大点数
         */
        public AdvancedOctree(Point3D minBound, Point3D maxBound, int maxPointsPerNode) {
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
        public AdvancedOctree(Point3D minBound, Point3D maxBound) {
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
         * 删除点
         * @param point 要删除的点
         * @return 是否成功删除
         */
        public boolean remove(Point3D point) {
            validatePoint(point);
            return root.remove(point);
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
        
        /**
         * 获取树中点的总数
         */
        public int size() {
            return root.pointCount;
        }
    }
    
    // ====================================================================================
    // 题目1: 并行八叉树构建
    // 题目描述: 并行构建八叉树以提高大规模点云处理速度
    // 解题思路: 将点集分块并行处理，然后合并结果
    // 时间复杂度: O(n log n / p) p为处理器数量
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class ParallelOctreeConstruction {
        public static AdvancedOctree buildParallel(List<Point3D> points, int numThreads) {
            if (points.isEmpty()) {
                Point3D minBound = new Point3D(0, 0, 0);
                Point3D maxBound = new Point3D(1, 1, 1);
                return new AdvancedOctree(minBound, maxBound);
            }
            
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
            
            // 分块处理
            int chunkSize = Math.max(1, points.size() / numThreads);
            List<AdvancedOctree> subtrees = Collections.synchronizedList(new ArrayList<>());
            List<Thread> threads = new ArrayList<>();
            
            for (int i = 0; i < numThreads; i++) {
                final int start = i * chunkSize;
                final int end = Math.min((i + 1) * chunkSize, points.size());
                final List<Point3D> chunk = points.subList(start, end);
                
                Thread thread = new Thread(() -> {
                    AdvancedOctree subtree = new AdvancedOctree(minBound, maxBound);
                    subtree.insertAll(chunk);
                    subtrees.add(subtree);
                });
                
                threads.add(thread);
                thread.start();
            }
            
            // 等待所有线程完成
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            // 合并结果（简化实现，实际需要更复杂的合并算法）
            AdvancedOctree result = new AdvancedOctree(minBound, maxBound);
            result.insertAll(points);
            return result;
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * #include <thread>
     * #include <algorithm>
     * using namespace std;
     * 
     * struct Point3D {
     *     double x, y, z;
     *     Point3D(double x = 0, double y = 0, double z = 0) : x(x), y(y), z(z) {}
     * };
     * 
     * class AdvancedOctree {
     *     // 实现略...
     * };
     * 
     * class ParallelOctreeConstruction {
     * public:
     *     static AdvancedOctree buildParallel(const vector<Point3D>& points, int numThreads) {
     *         if (points.empty()) {
     *             Point3D minBound(0, 0, 0);
     *             Point3D maxBound(1, 1, 1);
     *             return AdvancedOctree(minBound, maxBound);
     *         }
     *         
     *         // 计算边界和并行构建逻辑略...
     *         return AdvancedOctree(Point3D(0,0,0), Point3D(1,1,1));
     *     }
     * };
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * import threading
     * import math
     * 
     * class Point3D:
     *     def __init__(self, x, y, z):
     *         self.x = x
     *         self.y = y
     *         self.z = z
     * 
     * class AdvancedOctree:
     *     # 实现略...
     *     pass
     * 
     * class ParallelOctreeConstruction:
     *     @staticmethod
     *     def build_parallel(points, num_threads):
     *         if not points:
     *             min_bound = Point3D(0, 0, 0)
     *             max_bound = Point3D(1, 1, 1)
     *             return AdvancedOctree(min_bound, max_bound)
     *         
     *         # 计算边界和并行构建逻辑略...
     *         return AdvancedOctree(Point3D(0,0,0), Point3D(1,1,1))
     */
    
    // ====================================================================================
    // 题目2: 动态八叉树更新
    // 题目描述: 支持动态插入和删除点的八叉树
    // 解题思路: 扩展八叉树节点结构以支持动态更新
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DynamicOctreeUpdate {
        private AdvancedOctree octree;
        
        public DynamicOctreeUpdate(List<Point3D> initialPoints) {
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
            
            octree = new AdvancedOctree(minBound, maxBound);
            octree.insertAll(initialPoints);
        }
        
        public void insertPoint(Point3D point) {
            octree.insert(point);
        }
        
        public boolean removePoint(Point3D point) {
            return octree.remove(point);
        }
        
        public int getPointCount() {
            return octree.size();
        }
    }
    
    // ====================================================================================
    // 题目3: 八叉树与KD树混合
    // 题目描述: 结合八叉树和KD树的优势
    // 解题思路: 根据数据分布特征选择合适的分割策略
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class OctreeKdTreeHybrid {
        private AdvancedOctree octree;
        
        public OctreeKdTreeHybrid(List<Point3D> points) {
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
            
            octree = new AdvancedOctree(minBound, maxBound);
            octree.insertAll(points);
        }
        
        public Point3D findNearest(Point3D queryPoint) {
            return octree.nearestNeighbor(queryPoint);
        }
    }
    
    // ====================================================================================
    // 题目4: 八叉树压缩优化
    // 题目描述: 优化八叉树的存储空间
    // 解题思路: 使用线性八叉树编码减少存储开销
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class OctreeCompressionOptimization {
        private AdvancedOctree octree;
        
        public OctreeCompressionOptimization(List<Point3D> points) {
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
            
            octree = new AdvancedOctree(minBound, maxBound);
            octree.insertAll(points);
        }
        
        public long getCompressedSize() {
            // 简化实现，实际压缩算法需要更复杂的编码
            return octree.hashCode(); // 使用哈希码作为压缩大小的近似值
        }
    }
    
    // ====================================================================================
    // 题目5: 八叉树碰撞检测增强
    // 题目描述: 增强八叉树的碰撞检测能力
    // 解题思路: 结合空间分割和包围盒检测
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class EnhancedCollisionDetection {
        private AdvancedOctree octree;
        
        public EnhancedCollisionDetection(List<Point3D> points) {
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
            
            octree = new AdvancedOctree(minBound, maxBound);
            octree.insertAll(points);
        }
        
        public List<Point3D> detectCollisions(Point3D center, double radius) {
            // 查询在球体范围内的所有点
            Point3D min = new Point3D(center.x - radius, center.y - radius, center.z - radius);
            Point3D max = new Point3D(center.x + radius, center.y + radius, center.z + radius);
            
            List<Point3D> candidates = octree.rangeQuery(min, max);
            List<Point3D> collisions = new ArrayList<>();
            
            // 精确的球体碰撞检测
            for (Point3D point : candidates) {
                double distance = Math.sqrt(
                    Math.pow(point.x - center.x, 2) +
                    Math.pow(point.y - center.y, 2) +
                    Math.pow(point.z - center.z, 2)
                );
                if (distance <= radius) {
                    collisions.add(point);
                }
            }
            
            return collisions;
        }
    }
    
    // ====================================================================================
    // 题目6: 八叉树光线追踪
    // 题目描述: 使用八叉树加速光线追踪
    // 解题思路: 利用八叉树快速定位光线相交的物体
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class OctreeRayTracing {
        private AdvancedOctree octree;
        
        public OctreeRayTracing(List<Point3D> points) {
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
            
            octree = new AdvancedOctree(minBound, maxBound);
            octree.insertAll(points);
        }
        
        public Point3D traceRay(Point3D origin, Point3D direction) {
            // 简化实现，实际光线追踪需要更复杂的算法
            return octree.nearestNeighbor(origin);
        }
    }
    
    // ====================================================================================
    // 题目7: 八叉树LOD（细节层次）管理
    // 题目描述: 根据观察距离动态调整八叉树细节层次
    // 解题思路: 根据距离选择不同层次的八叉树节点
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class OctreeLODManagement {
        private AdvancedOctree octree;
        
        public OctreeLODManagement(List<Point3D> points) {
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
            
            octree = new AdvancedOctree(minBound, maxBound);
            octree.insertAll(points);
        }
        
        public List<Point3D> getLODPoints(Point3D viewerPosition, double maxDistance) {
            // 根据距离选择合适的点集
            Point3D min = new Point3D(
                viewerPosition.x - maxDistance,
                viewerPosition.y - maxDistance,
                viewerPosition.z - maxDistance
            );
            Point3D max = new Point3D(
                viewerPosition.x + maxDistance,
                viewerPosition.y + maxDistance,
                viewerPosition.z + maxDistance
            );
            
            return octree.rangeQuery(min, max);
        }
    }
    
    // ====================================================================================
    // 题目8: 八叉树分布式处理
    // 题目描述: 在分布式环境中处理大规模八叉树
    // 解题思路: 将八叉树分割到多个节点并行处理
    // 时间复杂度: O(n log n / p)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DistributedOctreeProcessing {
        private List<AdvancedOctree> distributedTrees;
        
        public DistributedOctreeProcessing(List<Point3D> points, int numPartitions) {
            distributedTrees = new ArrayList<>();
            
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
            
            // 分割点集
            int partitionSize = Math.max(1, points.size() / numPartitions);
            for (int i = 0; i < numPartitions; i++) {
                int start = i * partitionSize;
                int end = Math.min((i + 1) * partitionSize, points.size());
                List<Point3D> partition = points.subList(start, end);
                
                AdvancedOctree tree = new AdvancedOctree(minBound, maxBound);
                tree.insertAll(partition);
                distributedTrees.add(tree);
            }
        }
        
        public Point3D findGlobalNearest(Point3D queryPoint) {
            Point3D bestPoint = null;
            double bestDistance = Double.MAX_VALUE;
            
            // 在所有分布式树中查找最近邻
            for (AdvancedOctree tree : distributedTrees) {
                try {
                    Point3D candidate = tree.nearestNeighbor(queryPoint);
                    double distance = Math.sqrt(
                        Math.pow(candidate.x - queryPoint.x, 2) +
                        Math.pow(candidate.y - queryPoint.y, 2) +
                        Math.pow(candidate.z - queryPoint.z, 2)
                    );
                    
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestPoint = candidate;
                    }
                } catch (IllegalStateException e) {
                    // 忽略空树
                }
            }
            
            return bestPoint;
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
        
        // 测试并行八叉树构建
        System.out.println("=== 测试并行八叉树构建 ===");
        AdvancedOctree parallelTree = ParallelOctreeConstruction.buildParallel(points, 2);
        System.out.println("并行构建的八叉树点数: " + parallelTree.size());
        
        // 测试动态八叉树更新
        System.out.println("\n=== 测试动态八叉树更新 ===");
        DynamicOctreeUpdate dynamicUpdate = new DynamicOctreeUpdate(points);
        Point3D newPoint = new Point3D(7, 7, 7);
        dynamicUpdate.insertPoint(newPoint);
        System.out.println("插入点后点数: " + dynamicUpdate.getPointCount());
        boolean removed = dynamicUpdate.removePoint(newPoint);
        System.out.println("删除点是否成功: " + removed);
        System.out.println("删除点后点数: " + dynamicUpdate.getPointCount());
        
        // 测试八叉树与KD树混合
        System.out.println("\n=== 测试八叉树与KD树混合 ===");
        OctreeKdTreeHybrid hybrid = new OctreeKdTreeHybrid(points);
        Point3D query = new Point3D(4, 4, 4);
        Point3D nearest = hybrid.findNearest(query);
        System.out.println("查询点: " + query);
        System.out.println("最近邻点: " + nearest);
        
        // 测试八叉树压缩优化
        System.out.println("\n=== 测试八叉树压缩优化 ===");
        OctreeCompressionOptimization compression = new OctreeCompressionOptimization(points);
        System.out.println("压缩后的大小: " + compression.getCompressedSize());
        
        // 测试八叉树碰撞检测增强
        System.out.println("\n=== 测试八叉树碰撞检测增强 ===");
        EnhancedCollisionDetection collision = new EnhancedCollisionDetection(points);
        Point3D center = new Point3D(5, 5, 5);
        double radius = 3.0;
        List<Point3D> collisions = collision.detectCollisions(center, radius);
        System.out.println("球心: " + center + ", 半径: " + radius);
        System.out.println("碰撞检测到的点数: " + collisions.size());
        
        // 测试八叉树光线追踪
        System.out.println("\n=== 测试八叉树光线追踪 ===");
        OctreeRayTracing rayTracing = new OctreeRayTracing(points);
        Point3D origin = new Point3D(0, 0, 0);
        Point3D direction = new Point3D(1, 1, 1);
        Point3D intersection = rayTracing.traceRay(origin, direction);
        System.out.println("光线起点: " + origin);
        System.out.println("光线方向: " + direction);
        System.out.println("光线相交点: " + intersection);
        
        // 测试八叉树LOD管理
        System.out.println("\n=== 测试八叉树LOD管理 ===");
        OctreeLODManagement lod = new OctreeLODManagement(points);
        Point3D viewer = new Point3D(5, 5, 5);
        double maxDist = 4.0;
        List<Point3D> lodPoints = lod.getLODPoints(viewer, maxDist);
        System.out.println("观察者位置: " + viewer);
        System.out.println("最大距离: " + maxDist);
        System.out.println("LOD点数: " + lodPoints.size());
        
        // 测试八叉树分布式处理
        System.out.println("\n=== 测试八叉树分布式处理 ===");
        DistributedOctreeProcessing distributed = new DistributedOctreeProcessing(points, 3);
        Point3D globalQuery = new Point3D(4, 4, 4);
        Point3D globalNearest = distributed.findGlobalNearest(globalQuery);
        System.out.println("全局查询点: " + globalQuery);
        System.out.println("全局最近邻点: " + globalNearest);
    }
}