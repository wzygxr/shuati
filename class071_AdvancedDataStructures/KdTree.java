package class186;

import java.util.*;

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
public class KdTree {
    private Node root;
    private final int k; // 维度
    private final Random random = new Random();
    
    /**
     * K-D树节点定义
     */
    private static class Node {
        double[] point; // 数据点坐标
        Node left;      // 左子树
        Node right;     // 右子树
        int splitDim;   // 分割维度
        
        Node(double[] point, int splitDim) {
            this.point = point;
            this.splitDim = splitDim;
            this.left = null;
            this.right = null;
        }
    }
    
    /**
     * 构造函数
     * @param k 空间维度
     */
    public KdTree(int k) {
        this.k = k;
        this.root = null;
    }
    
    /**
     * 构建K-D树
     * @param points 数据点数组
     */
    public void build(double[][] points) {
        if (points == null || points.length == 0) {
            return;
        }
        // 验证所有点的维度是否一致
        for (double[] point : points) {
            if (point.length != k) {
                throw new IllegalArgumentException("所有点必须具有相同的维度：" + k);
            }
        }
        this.root = buildTree(Arrays.asList(points), 0);
    }
    
    /**
     * 递归构建K-D树
     * @param points 数据点列表
     * @param depth 当前深度
     * @return 构建好的子树根节点
     */
    private Node buildTree(List<double[]> points, int depth) {
        if (points.isEmpty()) {
            return null;
        }
        
        // 根据深度选择分割维度
        int splitDim = depth % k;
        
        // 根据分割维度对点进行排序
        points.sort(Comparator.comparingDouble(point -> point[splitDim]));
        
        // 选择中间点作为根节点
        int medianIndex = points.size() / 2;
        Node node = new Node(points.get(medianIndex), splitDim);
        
        // 递归构建左右子树
        node.left = buildTree(points.subList(0, medianIndex), depth + 1);
        node.right = buildTree(points.subList(medianIndex + 1, points.size()), depth + 1);
        
        return node;
    }
    
    /**
     * 范围查询
     * @param lowerBound 下界
     * @param upperBound 上界
     * @return 范围内的所有点
     */
    public List<double[]> rangeQuery(double[] lowerBound, double[] upperBound) {
        validateRange(lowerBound, upperBound);
        List<double[]> result = new ArrayList<>();
        rangeQuery(root, lowerBound, upperBound, result);
        return result;
    }
    
    /**
     * 递归执行范围查询
     * @param node 当前节点
     * @param lowerBound 下界
     * @param upperBound 上界
     * @param result 结果集
     */
    private void rangeQuery(Node node, double[] lowerBound, double[] upperBound, List<double[]> result) {
        if (node == null) {
            return;
        }
        
        // 检查当前点是否在范围内
        boolean inRange = true;
        for (int i = 0; i < k; i++) {
            if (node.point[i] < lowerBound[i] || node.point[i] > upperBound[i]) {
                inRange = false;
                break;
            }
        }
        
        if (inRange) {
            result.add(node.point.clone());
        }
        
        // 根据分割维度决定是否需要搜索左右子树
        int splitDim = node.splitDim;
        if (node.point[splitDim] >= lowerBound[splitDim]) {
            rangeQuery(node.left, lowerBound, upperBound, result);
        }
        if (node.point[splitDim] <= upperBound[splitDim]) {
            rangeQuery(node.right, lowerBound, upperBound, result);
        }
    }
    
    /**
     * 最近邻搜索
     * @param queryPoint 查询点
     * @return 最近的点
     */
    public double[] nearestNeighbor(double[] queryPoint) {
        validatePoint(queryPoint);
        if (root == null) {
            throw new IllegalStateException("K-D树为空");
        }
        
        Node bestNode = root;
        double bestDistance = distance(queryPoint, root.point);
        
        nearestNeighbor(root, queryPoint, bestDistance, new Node[] {bestNode});
        
        return bestNode.point.clone();
    }
    
    /**
     * 递归执行最近邻搜索
     * @param node 当前节点
     * @param queryPoint 查询点
     * @param bestDistance 当前最佳距离
     * @param bestNode 当前最佳节点引用
     * @return 更新后的最佳距离
     */
    private double nearestNeighbor(Node node, double[] queryPoint, double bestDistance, Node[] bestNode) {
        if (node == null) {
            return bestDistance;
        }
        
        // 计算当前点与查询点的距离
        double currentDistance = distance(queryPoint, node.point);
        if (currentDistance < bestDistance) {
            bestDistance = currentDistance;
            bestNode[0] = node;
        }
        
        int splitDim = node.splitDim;
        Node firstChild, secondChild;
        
        // 确定优先搜索的子树
        if (queryPoint[splitDim] < node.point[splitDim]) {
            firstChild = node.left;
            secondChild = node.right;
        } else {
            firstChild = node.right;
            secondChild = node.left;
        }
        
        // 优先搜索更可能包含最近点的子树
        bestDistance = nearestNeighbor(firstChild, queryPoint, bestDistance, bestNode);
        
        // 判断是否需要搜索另一个子树
        double planeDistance = Math.abs(queryPoint[splitDim] - node.point[splitDim]);
        if (planeDistance < bestDistance) {
            bestDistance = nearestNeighbor(secondChild, queryPoint, bestDistance, bestNode);
        }
        
        return bestDistance;
    }
    
    /**
     * 计算两个点之间的欧几里得距离
     * @param p1 第一个点
     * @param p2 第二个点
     * @return 距离
     */
    private double distance(double[] p1, double[] p2) {
        double sum = 0;
        for (int i = 0; i < k; i++) {
            double diff = p1[i] - p2[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
    
    /**
     * 验证范围参数
     * @param lowerBound 下界
     * @param upperBound 上界
     */
    private void validateRange(double[] lowerBound, double[] upperBound) {
        if (lowerBound == null || upperBound == null) {
            throw new IllegalArgumentException("范围参数不能为null");
        }
        if (lowerBound.length != k || upperBound.length != k) {
            throw new IllegalArgumentException("范围参数维度必须为：" + k);
        }
        for (int i = 0; i < k; i++) {
            if (lowerBound[i] > upperBound[i]) {
                throw new IllegalArgumentException("下界不能大于上界");
            }
        }
    }
    
    /**
     * 验证点参数
     * @param point 点
     */
    private void validatePoint(double[] point) {
        if (point == null) {
            throw new IllegalArgumentException("点不能为null");
        }
        if (point.length != k) {
            throw new IllegalArgumentException("点的维度必须为：" + k);
        }
    }
    
    /**
     * 获取树的高度
     * @return 树的高度
     */
    public int height() {
        return height(root);
    }
    
    /**
     * 递归计算树高
     * @param node 当前节点
     * @return 子树高度
     */
    private int height(Node node) {
        if (node == null) {
            return 0;
        }
        return Math.max(height(node.left), height(node.right)) + 1;
    }
    
    /**
     * 获取树中的节点数
     * @return 节点数
     */
    public int size() {
        return size(root);
    }
    
    /**
     * 递归计算节点数
     * @param node 当前节点
     * @return 子树节点数
     */
    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        return size(node.left) + size(node.right) + 1;
    }
    
    /**
     * 主方法，用于测试K-D树的功能
     */
    public static void main(String[] args) {
        // 创建一个2维的K-D树
        KdTree kdTree = new KdTree(2);
        
        // 构建数据点
        double[][] points = {
            {2.0, 3.0},
            {5.0, 4.0},
            {9.0, 6.0},
            {4.0, 7.0},
            {8.0, 1.0},
            {7.0, 2.0}
        };
        
        // 构建K-D树
        kdTree.build(points);
        
        System.out.println("K-D树构建完成，高度：" + kdTree.height());
        System.out.println("节点数量：" + kdTree.size());
        
        // 测试范围查询
        double[] lowerBound = {3.0, 2.0};
        double[] upperBound = {8.0, 6.0};
        List<double[]> rangeResult = kdTree.rangeQuery(lowerBound, upperBound);
        System.out.println("\n范围查询结果 [(3,2) 到 (8,6)]:");
        for (double[] point : rangeResult) {
            System.out.println(Arrays.toString(point));
        }
        
        // 测试最近邻搜索
        double[] queryPoint = {3.5, 4.5};
        double[] nearest = kdTree.nearestNeighbor(queryPoint);
        System.out.println("\n查询点 " + Arrays.toString(queryPoint) + " 的最近邻：" + Arrays.toString(nearest));
        
        // 边界情况测试
        try {
            double[] invalidPoint = {1.0}; // 维度不匹配
            kdTree.nearestNeighbor(invalidPoint);
        } catch (IllegalArgumentException e) {
            System.out.println("\n异常测试成功：" + e.getMessage());
        }
    }
}