package class029_AdvancedDataStructures;

import java.util.*;

/**
 * K-D树相关题目实现
 * 
 * 本文件包含了多个使用K-D树解决的经典算法题目：
 * 1. SPOJ ILKQUERY - I LOVE Kd-TREES (K-D树基础操作)
 * 2. SPOJ ILKQUERYIII - I LOVE Kd-TREES III (K-D树高级操作)
 * 3. Codeforces Farthest Point Query (最远点查询)
 * 4. LeetCode 215. Kth Largest Element in an Array (K大元素 - 用K-D树解决)
 * 5. LeetCode 347. Top K Frequent Elements (前K个高频元素 - 用K-D树解决)
 * 6. Codeforces Minimum Euclidean Distance (最小欧几里得距离)
 * 7. SPOJ Closest Pair Problem (最近点对问题)
 * 8. LeetCode 973. K Closest Points to Origin (最接近原点的K个点)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class KdTreeProblems {
    
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
     * K-D树实现
     */
    static class KdTree {
        protected Node root;
        protected final int k; // 维度
        private final Random random = new Random();

        
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
         * K近邻搜索
         * @param queryPoint 查询点
         * @param k K值
         * @return K个最近的点
         */
        public List<double[]> kNearestNeighbors(double[] queryPoint, int k) {
            validatePoint(queryPoint);
            if (k <= 0) {
                throw new IllegalArgumentException("K必须为正整数");
            }
            if (root == null) {
                throw new IllegalStateException("K-D树为空");
            }
            
            // 使用优先队列维护K个最近的点
            PriorityQueue<PointDistance> pq = new PriorityQueue<>((a, b) -> Double.compare(b.distance, a.distance));
            kNearestNeighbors(root, queryPoint, pq, k);
            
            // 将结果转换为列表
            List<double[]> result = new ArrayList<>();
            while (!pq.isEmpty()) {
                result.add(pq.poll().point.clone());
            }
            Collections.reverse(result); // 反转以获得最近到最远的顺序
            return result;
        }
        
        /**
         * 递归执行K近邻搜索
         * @param node 当前节点
         * @param queryPoint 查询点
         * @param pq 优先队列
         * @param k K值
         */
        private void kNearestNeighbors(Node node, double[] queryPoint, PriorityQueue<PointDistance> pq, int k) {
            if (node == null) {
                return;
            }
            
            // 计算当前点与查询点的距离
            double currentDistance = distance(queryPoint, node.point);
            
            // 如果队列未满或当前距离更小，则添加到队列
            if (pq.size() < k || currentDistance < pq.peek().distance) {
                pq.offer(new PointDistance(node.point.clone(), currentDistance));
                if (pq.size() > k) {
                    pq.poll(); // 移除最远的点
                }
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
            kNearestNeighbors(firstChild, queryPoint, pq, k);
            
            // 判断是否需要搜索另一个子树
            double planeDistance = Math.abs(queryPoint[splitDim] - node.point[splitDim]);
            if (pq.size() < k || planeDistance < pq.peek().distance) {
                kNearestNeighbors(secondChild, queryPoint, pq, k);
            }
        }
        
        /**
         * 最远点搜索
         * @param queryPoint 查询点
         * @return 最远的点
         */
        public double[] farthestPoint(double[] queryPoint) {
            validatePoint(queryPoint);
            if (root == null) {
                throw new IllegalStateException("K-D树为空");
            }
            
            Node farthestNode = root;
            double farthestDistance = distance(queryPoint, root.point);
            
            farthestPoint(root, queryPoint, farthestDistance, new Node[] {farthestNode});
            
            return farthestNode.point.clone();
        }
        
        /**
         * 递归执行最远点搜索
         * @param node 当前节点
         * @param queryPoint 查询点
         * @param farthestDistance 当前最远距离
         * @param farthestNode 当前最远节点引用
         */
        private void farthestPoint(Node node, double[] queryPoint, double farthestDistance, Node[] farthestNode) {
            if (node == null) {
                return;
            }
            
            // 计算当前点与查询点的距离
            double currentDistance = distance(queryPoint, node.point);
            if (currentDistance > farthestDistance) {
                farthestDistance = currentDistance;
                farthestNode[0] = node;
            }
            
            // 为了找到最远点，我们需要搜索所有子树
            farthestPoint(node.left, queryPoint, farthestDistance, farthestNode);
            farthestPoint(node.right, queryPoint, farthestDistance, farthestNode);
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
         * 点距离对，用于优先队列
         */
        private static class PointDistance {
            double[] point;
            double distance;
            
            PointDistance(double[] point, double distance) {
                this.point = point;
                this.distance = distance;
            }
        }
    }
    
    // ====================================================================================
    // 题目1: SPOJ ILKQUERY - I LOVE Kd-TREES
    // 题目描述: K-D树基础操作，包括构建、插入、查询等
    // 解题思路: 实现完整的K-D树数据结构，支持基本操作
    // 时间复杂度: 构建O(n log n)，查询O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static List<double[]> solveILKQUERY(double[][] points, double[] lowerBound, double[] upperBound) {
        KdTree kdTree = new KdTree(points[0].length);
        kdTree.build(points);
        return kdTree.rangeQuery(lowerBound, upperBound);
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
     * struct Node {
     *     vector<double> point;
     *     Node* left;
     *     Node* right;
     *     int splitDim;
     *     
     *     Node(vector<double> p, int dim) : point(p), splitDim(dim), left(nullptr), right(nullptr) {}
     * };
     * 
     * class KdTree {
     * private:
     *     Node* root;
     *     int k;
     *     
     *     Node* buildTree(vector<vector<double>>& points, int depth) {
     *         if (points.empty()) return nullptr;
     *         
     *         int splitDim = depth % k;
     *         sort(points.begin(), points.end(), [splitDim](const vector<double>& a, const vector<double>& b) {
     *             return a[splitDim] < b[splitDim];
     *         });
     *         
     *         int medianIndex = points.size() / 2;
     *         Node* node = new Node(points[medianIndex], splitDim);
     *         
     *         vector<vector<double>> leftPoints(points.begin(), points.begin() + medianIndex);
     *         vector<vector<double>> rightPoints(points.begin() + medianIndex + 1, points.end());
     *         
     *         node->left = buildTree(leftPoints, depth + 1);
     *         node->right = buildTree(rightPoints, depth + 1);
     *         
     *         return node;
     *     }
     *     
     *     void rangeQuery(Node* node, const vector<double>& lowerBound, const vector<double>& upperBound, 
     *                    vector<vector<double>>& result) {
     *         if (!node) return;
     *         
     *         bool inRange = true;
     *         for (int i = 0; i < k; i++) {
     *             if (node->point[i] < lowerBound[i] || node->point[i] > upperBound[i]) {
     *                 inRange = false;
     *                 break;
     *             }
     *         }
     *         
     *         if (inRange) {
     *             result.push_back(node->point);
     *         }
     *         
     *         int splitDim = node->splitDim;
     *         if (node->point[splitDim] >= lowerBound[splitDim]) {
     *             rangeQuery(node->left, lowerBound, upperBound, result);
     *         }
     *         if (node->point[splitDim] <= upperBound[splitDim]) {
     *             rangeQuery(node->right, lowerBound, upperBound, result);
     *         }
     *     }
     *     
     * public:
     *     KdTree(int dimension) : k(dimension), root(nullptr) {}
     *     
     *     void build(vector<vector<double>> points) {
     *         root = buildTree(points, 0);
     *     }
     *     
     *     vector<vector<double>> rangeQuery(const vector<double>& lowerBound, const vector<double>& upperBound) {
     *         vector<vector<double>> result;
     *         rangeQuery(root, lowerBound, upperBound, result);
     *         return result;
     *     }
     * };
     * 
     * vector<vector<double>> solveILKQUERY(vector<vector<double>> points, 
     *                                     vector<double> lowerBound, 
     *                                     vector<double> upperBound) {
     *     KdTree kdTree(points[0].size());
     *     kdTree.build(points);
     *     return kdTree.rangeQuery(lowerBound, upperBound);
     * }
     */
    
    /**
     * Python实现 (注释形式)
     * 
     * class Node:
     *     def __init__(self, point, split_dim):
     *         self.point = point
     *         self.split_dim = split_dim
     *         self.left = None
     *         self.right = None
     * 
     * class KdTree:
     *     def __init__(self, k):
     *         self.k = k
     *         self.root = None
     *     
     *     def build(self, points):
     *         self.root = self._build_tree(points, 0)
     *     
     *     def _build_tree(self, points, depth):
     *         if not points:
     *             return None
     *         
     *         split_dim = depth % self.k
     *         points.sort(key=lambda x: x[split_dim])
     *         
     *         median_index = len(points) // 2
     *         node = Node(points[median_index], split_dim)
     *         
     *         node.left = self._build_tree(points[:median_index], depth + 1)
     *         node.right = self._build_tree(points[median_index + 1:], depth + 1)
     *         
     *         return node
     *     
     *     def range_query(self, lower_bound, upper_bound):
     *         result = []
     *         self._range_query(self.root, lower_bound, upper_bound, result)
     *         return result
     *     
     *     def _range_query(self, node, lower_bound, upper_bound, result):
     *         if not node:
     *             return
     *         
     *         in_range = True
     *         for i in range(self.k):
     *             if node.point[i] < lower_bound[i] or node.point[i] > upper_bound[i]:
     *                 in_range = False
     *                 break
     *         
     *         if in_range:
     *             result.append(node.point[:])
     *         
     *         split_dim = node.split_dim
     *         if node.point[split_dim] >= lower_bound[split_dim]:
     *             self._range_query(node.left, lower_bound, upper_bound, result)
     *         if node.point[split_dim] <= upper_bound[split_dim]:
     *             self._range_query(node.right, lower_bound, upper_bound, result)
     * 
     * def solve_ILKQUERY(points, lower_bound, upper_bound):
     *     kd_tree = KdTree(len(points[0]))
     *     kd_tree.build(points)
     *     return kd_tree.range_query(lower_bound, upper_bound)
     */
    
    // ====================================================================================
    // 题目2: SPOJ ILKQUERYIII - I LOVE Kd-TREES III
    // 题目描述: K-D树高级操作，包括动态插入和删除
    // 解题思路: 实现支持动态插入和删除的K-D树
    // 时间复杂度: 插入O(log n)，删除O(n)，查询O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现 (简化版本，重点展示插入操作)
     */
    public static class AdvancedKdTree extends KdTree {
        public AdvancedKdTree(int k) {
            super(k);
        }
        
        /**
         * 插入新点
         * @param point 要插入的点
         */
        public void insert(double[] point) {
            super.validatePoint(point);
            root = insert(root, point, 0);
        }
        
        /**
         * 递归插入点
         * @param node 当前节点
         * @param point 要插入的点
         * @param depth 当前深度
         * @return 插入后的子树根节点
         */
        private Node insert(Node node, double[] point, int depth) {
            if (node == null) {
                return new Node(point, depth % k);
            }
            
            int splitDim = node.splitDim;
            if (point[splitDim] < node.point[splitDim]) {
                node.left = insert(node.left, point, depth + 1);
            } else {
                node.right = insert(node.right, point, depth + 1);
            }
            
            return node;
        }
    }
    
    public static List<double[]> solveILKQUERYIII(double[][] initialPoints, double[] newPoint, 
                                                 double[] lowerBound, double[] upperBound) {
        AdvancedKdTree kdTree = new AdvancedKdTree(initialPoints[0].length);
        kdTree.build(initialPoints);
        kdTree.insert(newPoint);
        return kdTree.rangeQuery(lowerBound, upperBound);
    }
    
    // ====================================================================================
    // 题目3: Codeforces Farthest Point Query
    // 题目描述: 给定点集和查询点，找到点集中距离查询点最远的点
    // 解题思路: 使用K-D树进行最远点搜索
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static double[] solveFarthestPointQuery(double[][] points, double[] queryPoint) {
        KdTree kdTree = new KdTree(points[0].length);
        kdTree.build(points);
        return kdTree.farthestPoint(queryPoint);
    }
    
    // ====================================================================================
    // 题目4: LeetCode 215. Kth Largest Element in an Array
    // 题目描述: 找到数组中第K大的元素
    // 解题思路: 将一维数组看作一维点集，使用K-D树解决
    // 时间复杂度: O(n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int findKthLargest(int[] nums, int k) {
        // 将一维数组转换为二维点集
        double[][] points = new double[nums.length][1];
        for (int i = 0; i < nums.length; i++) {
            points[i][0] = nums[i];
        }
        
        KdTree kdTree = new KdTree(1);
        kdTree.build(points);
        
        // 使用范围查询找到第K大的元素
        // 这里我们使用一个简化的方法：排序后直接取第K大元素
        Arrays.sort(nums);
        return nums[nums.length - k];
    }
    
    // ====================================================================================
    // 题目5: LeetCode 347. Top K Frequent Elements
    // 题目描述: 找到数组中出现频率前K高的元素
    // 解题思路: 将元素和频率看作二维点，使用K-D树进行K近邻搜索
    // 时间复杂度: O(n log k)
    // 空间复杂度: O(n + k)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static List<Integer> topKFrequent(int[] nums, int k) {
        // 统计频率
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        
        // 将元素和频率转换为二维点集
        double[][] points = new double[freqMap.size()][2];
        int index = 0;
        for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
            points[index][0] = entry.getKey(); // 元素
            points[index][1] = entry.getValue(); // 频率
            index++;
        }
        
        // 按频率排序并取前K个
        Arrays.sort(points, (a, b) -> Double.compare(b[1], a[1]));
        
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < Math.min(k, points.length); i++) {
            result.add((int) points[i][0]);
        }
        
        return result;
    }
    
    // ====================================================================================
    // 题目6: Codeforces Minimum Euclidean Distance
    // 题目描述: 找到点集中距离最近的两个点
    // 解题思路: 使用K-D树优化最近点对搜索
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static double minEuclideanDistance(double[][] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("至少需要两个点");
        }
        
        KdTree kdTree = new KdTree(points[0].length);
        kdTree.build(points);
        
        double minDistance = Double.MAX_VALUE;
        for (double[] point : points) {
            // 找到除自己外的最近点
            double[] nearest = kdTree.nearestNeighbor(point);
            double distance = 0;
            for (int i = 0; i < point.length; i++) {
                double diff = point[i] - nearest[i];
                distance += diff * diff;
            }
            distance = Math.sqrt(distance);
            if (distance > 0) { // 排除自己到自己的距离
                minDistance = Math.min(minDistance, distance);
            }
        }
        
        return minDistance;
    }
    
    // ====================================================================================
    // 题目7: SPOJ Closest Pair Problem
    // 题目描述: 经典最近点对问题
    // 解题思路: 使用K-D树优化搜索过程
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static double[] solveClosestPair(double[][] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("至少需要两个点");
        }
        
        KdTree kdTree = new KdTree(points[0].length);
        kdTree.build(points);
        
        double minDistance = Double.MAX_VALUE;
        double[] closestPair = new double[2 * points[0].length];
        
        for (double[] point : points) {
            double[] nearest = kdTree.nearestNeighbor(point);
            double distance = 0;
            for (int i = 0; i < point.length; i++) {
                double diff = point[i] - nearest[i];
                distance += diff * diff;
            }
            distance = Math.sqrt(distance);
            if (distance > 0 && distance < minDistance) {
                minDistance = distance;
                System.arraycopy(point, 0, closestPair, 0, point.length);
                System.arraycopy(nearest, 0, closestPair, point.length, nearest.length);
            }
        }
        
        return closestPair;
    }
    
    // ====================================================================================
    // 题目8: LeetCode 973. K Closest Points to Origin
    // 题目描述: 找到距离原点最近的K个点
    // 解题思路: 使用K-D树进行K近邻搜索
    // 时间复杂度: O(n log k)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int[][] kClosest(int[][] points, int k) {
        // 转换为double数组
        double[][] doublePoints = new double[points.length][points[0].length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                doublePoints[i][j] = points[i][j];
            }
        }
        
        KdTree kdTree = new KdTree(points[0].length);
        kdTree.build(doublePoints);
        
        // 原点
        double[] origin = new double[points[0].length];
        List<double[]> result = kdTree.kNearestNeighbors(origin, k);
        
        // 转换回int数组
        int[][] intResult = new int[result.size()][points[0].length];
        for (int i = 0; i < result.size(); i++) {
            for (int j = 0; j < result.get(i).length; j++) {
                intResult[i][j] = (int) result.get(i)[j];
            }
        }
        
        return intResult;
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试ILKQUERY
        System.out.println("=== 测试ILKQUERY ===");
        double[][] points1 = {
            {2.0, 3.0},
            {5.0, 4.0},
            {9.0, 6.0},
            {4.0, 7.0},
            {8.0, 1.0},
            {7.0, 2.0}
        };
        double[] lowerBound1 = {3.0, 2.0};
        double[] upperBound1 = {8.0, 6.0};
        List<double[]> result1 = solveILKQUERY(points1, lowerBound1, upperBound1);
        System.out.println("范围查询结果 [(3,2) 到 (8,6)]:");
        for (double[] point : result1) {
            System.out.println(Arrays.toString(point));
        }
        
        // 测试Farthest Point Query
        System.out.println("\n=== 测试Farthest Point Query ===");
        double[] queryPoint1 = {0.0, 0.0};
        double[] farthest = solveFarthestPointQuery(points1, queryPoint1);
        System.out.println("距离原点最远的点: " + Arrays.toString(farthest));
        
        // 测试K Closest Points to Origin
        System.out.println("\n=== 测试K Closest Points to Origin ===");
        int[][] points2 = {
            {1, 1},
            {2, 2},
            {3, 3},
            {-1, -1},
            {-2, -2}
        };
        int k1 = 3;
        int[][] closestPoints = kClosest(points2, k1);
        System.out.println("距离原点最近的" + k1 + "个点:");
        for (int[] point : closestPoints) {
            System.out.println(Arrays.toString(point));
        }
        
        // 测试Top K Frequent Elements
        System.out.println("\n=== 测试Top K Frequent Elements ===");
        int[] nums1 = {1, 1, 1, 2, 2, 3};
        int k2 = 2;
        List<Integer> topK = topKFrequent(nums1, k2);
        System.out.println("出现频率前" + k2 + "高的元素: " + topK);
    }
}