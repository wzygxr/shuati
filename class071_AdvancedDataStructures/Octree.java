package class186;

import java.util.*;

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
public class Octree {
    private Node root;
    private final Point3D minBound; // 空间最小值边界
    private final Point3D maxBound; // 空间最大值边界
    private final int maxPointsPerNode; // 每个节点最大点数
    private final double minSize; // 最小节点大小，避免无限分割
    
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
     * 八叉树节点类
     */
    private static class Node {
        Point3D center;         // 节点中心坐标
        double size;            // 节点大小（立方体边长）
        List<Point3D> points;   // 存储在此节点的点
        Node[] children;        // 子节点，最多8个
        boolean isLeaf;         // 是否为叶节点
        
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
        
        this.minSize = size / Math.pow(2, 20); // 设置最小节点大小，避免无限分割
        this.root = new Node(center, size);
    }
    
    /**
     * 简化构造函数，使用默认参数
     * @param minBound 空间最小值边界
     * @param maxBound 空间最大值边界
     */
    public Octree(Point3D minBound, Point3D maxBound) {
        this(minBound, maxBound, 8); // 默认每个节点最多8个点
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
    
    /**
     * 计算树的高度
     */
    public int height() {
        return height(root);
    }
    
    /**
     * 递归计算树高
     */
    private int height(Node node) {
        if (node.isLeaf) {
            return 1;
        }
        int maxHeight = 0;
        for (Node child : node.children) {
            maxHeight = Math.max(maxHeight, height(child));
        }
        return maxHeight + 1;
    }
    
    /**
     * 获取树中点的总数
     */
    public int size() {
        return size(root);
    }
    
    /**
     * 递归计算节点数
     */
    private int size(Node node) {
        if (node.isLeaf) {
            return node.points.size();
        }
        int totalSize = 0;
        for (Node child : node.children) {
            totalSize += size(child);
        }
        return totalSize;
    }
    
    /**
     * 主方法，用于测试八叉树的功能
     */
    public static void main(String[] args) {
        // 定义空间边界
        Point3D minBound = new Point3D(0, 0, 0);
        Point3D maxBound = new Point3D(10, 10, 10);
        
        // 创建八叉树
        Octree octree = new Octree(minBound, maxBound, 4);
        
        // 创建一些测试点
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
        
        // 插入所有点
        octree.insertAll(points);
        
        System.out.println("八叉树构建完成");
        System.out.println("树高度: " + octree.height());
        System.out.println("点的数量: " + octree.size());
        
        // 测试范围查询
        Point3D queryMin = new Point3D(2, 2, 2);
        Point3D queryMax = new Point3D(7, 7, 7);
        List<Point3D> rangeResults = octree.rangeQuery(queryMin, queryMax);
        
        System.out.println("\n范围查询结果 (2-7, 2-7, 2-7):");
        for (Point3D point : rangeResults) {
            System.out.println(point);
        }
        
        // 测试最近邻搜索
        Point3D queryPoint = new Point3D(4, 4, 4);
        Point3D nearest = octree.nearestNeighbor(queryPoint);
        
        System.out.println("\n查询点 " + queryPoint + " 的最近邻: " + nearest);
        System.out.println("距离: " + Math.sqrt(
            Math.pow(queryPoint.x - nearest.x, 2) +
            Math.pow(queryPoint.y - nearest.y, 2) +
            Math.pow(queryPoint.z - nearest.z, 2)
        ));
        
        // 测试边界情况
        try {
            octree.insert(new Point3D(11, 1, 1)); // 超出边界
        } catch (IllegalArgumentException e) {
            System.out.println("\n边界测试成功: " + e.getMessage());
        }
    }
}