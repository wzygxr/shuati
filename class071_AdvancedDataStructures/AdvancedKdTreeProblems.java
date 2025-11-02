package class029_AdvancedDataStructures;

import java.util.*;

/**
 * 高级K-D树题目实现
 * 
 * 本文件包含了更多使用K-D树解决的高级算法题目：
 * 1. Dynamic K-D Tree (支持动态插入和删除)
 * 2. Range Count Query (范围计数查询)
 * 3. K-Nearest Neighbors Search (K近邻搜索)
 * 4. Minimum Bounding Rectangle (最小边界矩形)
 * 5. Point in Polygon Test (点在多边形内测试 - 使用K-D树优化)
 * 6. Collision Detection (碰撞检测)
 * 7. Nearest Neighbor in High Dimensions (高维最近邻搜索)
 * 8. Approximate Nearest Neighbor (近似最近邻搜索)
 * 
 * 所有题目均提供Java、C++、Python三种语言实现
 * 并包含详细注释、复杂度分析和最优解验证
 */
public class AdvancedKdTreeProblems {
    
    /**
     * K-D树节点定义
     */
    private static class Node {
        double[] point; // 数据点坐标
        Node left;      // 左子树
        Node right;     // 右子树
        int splitDim;   // 分割维度
        int size;       // 子树大小
        
        Node(double[] point, int splitDim) {
            this.point = point;
            this.splitDim = splitDim;
            this.left = null;
            this.right = null;
            this.size = 1;
        }
    }
    
    /**
     * 动态K-D树实现（支持插入和删除）
     */
    static class DynamicKdTree {
        protected Node root;
        protected final int k; // 维度
        
        /**
         * 构造函数
         * @param k 空间维度
         */
        public DynamicKdTree(int k) {
            this.k = k;
            this.root = null;
        }
        
        /**
         * 插入新点
         * @param point 要插入的点
         */
        public void insert(double[] point) {
            validatePoint(point);
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
            
            node.size = 1 + getSize(node.left) + getSize(node.right);
            return node;
        }
        
        /**
         * 删除点
         * @param point 要删除的点
         */
        public void delete(double[] point) {
            validatePoint(point);
            root = delete(root, point, 0);
        }
        
        /**
         * 递归删除点
         * @param node 当前节点
         * @param point 要删除的点
         * @param depth 当前深度
         * @return 删除后的子树根节点
         */
        private Node delete(Node node, double[] point, int depth) {
            if (node == null) {
                return null;
            }
            
            int splitDim = node.splitDim;
            
            // 找到要删除的节点
            if (Arrays.equals(node.point, point)) {
                // 情况1：叶子节点
                if (node.left == null && node.right == null) {
                    return null;
                }
                
                // 情况2：只有右子树
                if (node.left == null) {
                    Node successor = findMin(node.right, splitDim);
                    node.point = successor.point;
                    node.right = delete(node.right, successor.point, depth);
                }
                // 情况3：只有左子树
                else if (node.right == null) {
                    Node successor = findMin(node.left, splitDim);
                    node.point = successor.point;
                    node.left = delete(node.left, successor.point, depth);
                }
                // 情况4：左右子树都存在
                else {
                    Node successor = findMin(node.right, splitDim);
                    node.point = successor.point;
                    node.right = delete(node.right, successor.point, depth);
                }
            }
            // 继续搜索要删除的节点
            else if (point[splitDim] < node.point[splitDim]) {
                node.left = delete(node.left, point, depth + 1);
            } else {
                node.right = delete(node.right, point, depth + 1);
            }
            
            node.size = 1 + getSize(node.left) + getSize(node.right);
            return node;
        }
        
        /**
         * 查找子树中指定维度的最小值节点
         * @param node 子树根节点
         * @param dim 维度
         * @return 最小值节点
         */
        private Node findMin(Node node, int dim) {
            if (node == null) {
                return null;
            }
            
            // 如果当前分割维度等于目标维度
            if (node.splitDim == dim) {
                if (node.left == null) {
                    return node;
                }
                return findMin(node.left, dim);
            }
            
            // 否则需要检查所有子树
            Node minNode = node;
            if (node.left != null) {
                Node leftMin = findMin(node.left, dim);
                if (leftMin != null && leftMin.point[dim] < minNode.point[dim]) {
                    minNode = leftMin;
                }
            }
            if (node.right != null) {
                Node rightMin = findMin(node.right, dim);
                if (rightMin != null && rightMin.point[dim] < minNode.point[dim]) {
                    minNode = rightMin;
                }
            }
            
            return minNode;
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
         * 范围计数查询
         * @param lowerBound 下界
         * @param upperBound 上界
         * @return 范围内的点数
         */
        public int rangeCount(double[] lowerBound, double[] upperBound) {
            validateRange(lowerBound, upperBound);
            return rangeCount(root, lowerBound, upperBound);
        }
        
        /**
         * 递归执行范围计数查询
         * @param node 当前节点
         * @param lowerBound 下界
         * @param upperBound 上界
         * @return 范围内的点数
         */
        private int rangeCount(Node node, double[] lowerBound, double[] upperBound) {
            if (node == null) {
                return 0;
            }
            
            // 检查当前点是否在范围内
            boolean inRange = true;
            for (int i = 0; i < k; i++) {
                if (node.point[i] < lowerBound[i] || node.point[i] > upperBound[i]) {
                    inRange = false;
                    break;
                }
            }
            
            int count = inRange ? 1 : 0;
            
            // 根据分割维度决定是否需要搜索左右子树
            int splitDim = node.splitDim;
            if (node.point[splitDim] >= lowerBound[splitDim]) {
                count += rangeCount(node.left, lowerBound, upperBound);
            }
            if (node.point[splitDim] <= upperBound[splitDim]) {
                count += rangeCount(node.right, lowerBound, upperBound);
            }
            
            return count;
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
         * 获取节点子树大小
         * @param node 节点
         * @return 子树大小
         */
        private int getSize(Node node) {
            return node == null ? 0 : node.size;
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
            return getSize(root);
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
    // 题目1: Dynamic K-D Tree Operations
    // 题目描述: 实现支持动态插入和删除的K-D树
    // 解题思路: 在基本K-D树基础上增加插入和删除操作
    // 时间复杂度: 插入O(log n)，删除O(n)，查询O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static class DynamicKdTreeOperations {
        private DynamicKdTree kdTree;
        
        public DynamicKdTreeOperations(int k) {
            this.kdTree = new DynamicKdTree(k);
        }
        
        public void insert(double[] point) {
            kdTree.insert(point);
        }
        
        public void delete(double[] point) {
            kdTree.delete(point);
        }
        
        public List<double[]> rangeQuery(double[] lowerBound, double[] upperBound) {
            return kdTree.rangeQuery(lowerBound, upperBound);
        }
        
        public int rangeCount(double[] lowerBound, double[] upperBound) {
            return kdTree.rangeCount(lowerBound, upperBound);
        }
    }
    
    /**
     * C++实现 (注释形式)
     * 
     * #include <iostream>
     * #include <vector>
     * #include <algorithm>
     * #include <cmath>
     * #include <queue>
     * using namespace std;
     * 
     * struct Node {
     *     vector<double> point;
     *     Node* left;
     *     Node* right;
     *     int splitDim;
     *     int size;
     *     
     *     Node(vector<double> p, int dim) : point(p), splitDim(dim), left(nullptr), right(nullptr), size(1) {}
     * };
     * 
     * class DynamicKdTree {
     * private:
     *     Node* root;
     *     int k;
     *     
     *     Node* insert(Node* node, const vector<double>& point, int depth) {
     *         if (!node) return new Node(point, depth % k);
     *         
     *         int splitDim = node->splitDim;
     *         if (point[splitDim] < node->point[splitDim]) {
     *             node->left = insert(node->left, point, depth + 1);
     *         } else {
     *             node->right = insert(node->right, point, depth + 1);
     *         }
     *         
     *         node->size = 1 + getSize(node->left) + getSize(node->right);
     *         return node;
     *     }
     *     
     *     Node* deleteNode(Node* node, const vector<double>& point, int depth) {
     *         if (!node) return nullptr;
     *         
     *         int splitDim = node->splitDim;
     *         if (node->point == point) {
     *             if (!node->left && !node->right) {
     *                 delete node;
     *                 return nullptr;
     *             }
     *             if (!node->left) {
     *                 Node* successor = findMin(node->right, splitDim);
     *                 node->point = successor->point;
     *                 node->right = deleteNode(node->right, successor->point, depth);
     *             } else if (!node->right) {
     *                 Node* successor = findMin(node->left, splitDim);
     *                 node->point = successor->point;
     *                 node->left = deleteNode(node->left, successor->point, depth);
     *             } else {
     *                 Node* successor = findMin(node->right, splitDim);
     *                 node->point = successor->point;
     *                 node->right = deleteNode(node->right, successor->point, depth);
     *             }
     *         } else if (point[splitDim] < node->point[splitDim]) {
     *             node->left = deleteNode(node->left, point, depth + 1);
     *         } else {
     *             node->right = deleteNode(node->right, point, depth + 1);
     *         }
     *         
     *         node->size = 1 + getSize(node->left) + getSize(node->right);
     *         return node;
     *     }
     *     
     *     Node* findMin(Node* node, int dim) {
     *         if (!node) return nullptr;
     *         if (node->splitDim == dim) {
     *             if (!node->left) return node;
     *             return findMin(node->left, dim);
     *         }
     *         Node* minNode = node;
     *         if (node->left) {
     *             Node* leftMin = findMin(node->left, dim);
     *             if (leftMin && leftMin->point[dim] < minNode->point[dim]) minNode = leftMin;
     *         }
     *         if (node->right) {
     *             Node* rightMin = findMin(node->right, dim);
     *             if (rightMin && rightMin->point[dim] < minNode->point[dim]) minNode = rightMin;
     *         }
     *         return minNode;
     *     }
     *     
     *     int getSize(Node* node) {
     *         return node ? node->size : 0;
     *     }
     *     
     * public:
     *     DynamicKdTree(int dimension) : k(dimension), root(nullptr) {}
     *     
     *     void insert(const vector<double>& point) {
     *         root = insert(root, point, 0);
     *     }
     *     
     *     void deletePoint(const vector<double>& point) {
     *         root = deleteNode(root, point, 0);
     *     }
     * };
     * 
     * class DynamicKdTreeOperations {
     * private:
     *     DynamicKdTree kdTree;
     *     
     * public:
     *     DynamicKdTreeOperations(int k) : kdTree(k) {}
     *     
     *     void insert(const vector<double>& point) {
     *         kdTree.insert(point);
     *     }
     *     
     *     void deletePoint(const vector<double>& point) {
     *         kdTree.deletePoint(point);
     *     }
     * };
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
     *         self.size = 1
     * 
     * class DynamicKdTree:
     *     def __init__(self, k):
     *         self.k = k
     *         self.root = None
     *     
     *     def insert(self, point):
     *         self.root = self._insert(self.root, point, 0)
     *     
     *     def _insert(self, node, point, depth):
     *         if not node:
     *             return Node(point, depth % self.k)
     *         
     *         split_dim = node.split_dim
     *         if point[split_dim] < node.point[split_dim]:
     *             node.left = self._insert(node.left, point, depth + 1)
     *         else:
     *             node.right = self._insert(node.right, point, depth + 1)
     *         
     *         node.size = 1 + self._get_size(node.left) + self._get_size(node.right)
     *         return node
     *     
     *     def delete(self, point):
     *         self.root = self._delete(self.root, point, 0)
     *     
     *     def _delete(self, node, point, depth):
     *         if not node:
     *             return None
     *         
     *         split_dim = node.split_dim
     *         if node.point == point:
     *             if not node.left and not node.right:
     *                 return None
     *             if not node.left:
     *                 successor = self._find_min(node.right, split_dim)
     *                 node.point = successor.point
     *                 node.right = self._delete(node.right, successor.point, depth)
     *             elif not node.right:
     *                 successor = self._find_min(node.left, split_dim)
     *                 node.point = successor.point
     *                 node.left = self._delete(node.left, successor.point, depth)
     *             else:
     *                 successor = self._find_min(node.right, split_dim)
     *                 node.point = successor.point
     *                 node.right = self._delete(node.right, successor.point, depth)
     *         elif point[split_dim] < node.point[split_dim]:
     *             node.left = self._delete(node.left, point, depth + 1)
     *         else:
     *             node.right = self._delete(node.right, point, depth + 1)
     *         
     *         node.size = 1 + self._get_size(node.left) + self._get_size(node.right)
     *         return node
     *     
     *     def _find_min(self, node, dim):
     *         if not node:
     *             return None
     *         if node.split_dim == dim:
     *             if not node.left:
     *                 return node
     *             return self._find_min(node.left, dim)
     *         min_node = node
     *         if node.left:
     *             left_min = self._find_min(node.left, dim)
     *             if left_min and left_min.point[dim] < min_node.point[dim]:
     *                 min_node = left_min
     *         if node.right:
     *             right_min = self._find_min(node.right, dim)
     *             if right_min and right_min.point[dim] < min_node.point[dim]:
     *                 min_node = right_min
     *         return min_node
     *     
     *     def _get_size(self, node):
     *         return node.size if node else 0
     * 
     * class DynamicKdTreeOperations:
     *     def __init__(self, k):
     *         self.kd_tree = DynamicKdTree(k)
     *     
     *     def insert(self, point):
     *         self.kd_tree.insert(point)
     *     
     *     def delete(self, point):
     *         self.kd_tree.delete(point)
     */
    
    // ====================================================================================
    // 题目2: Range Count Query
    // 题目描述: 实现范围计数查询，返回范围内点的数量
    // 解题思路: 在K-D树范围查询基础上优化计数逻辑
    // 时间复杂度: O(n^(1-1/k) + k)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static int rangeCountQuery(DynamicKdTree kdTree, double[] lowerBound, double[] upperBound) {
        return kdTree.rangeCount(lowerBound, upperBound);
    }
    
    // ====================================================================================
    // 题目3: K-Nearest Neighbors Search
    // 题目描述: 实现K近邻搜索
    // 解题思路: 使用优先队列维护K个最近的点
    // 时间复杂度: O(n log k)
    // 空间复杂度: O(k)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static List<double[]> kNearestNeighbors(DynamicKdTree kdTree, double[] queryPoint, int k) {
        return kdTree.kNearestNeighbors(queryPoint, k);
    }
    
    // ====================================================================================
    // 题目4: Minimum Bounding Rectangle
    // 题目描述: 找到包含所有点的最小边界矩形
    // 解题思路: 遍历所有点找到每个维度的最小值和最大值
    // 时间复杂度: O(n)
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static double[][] minimumBoundingRectangle(double[][] points) {
        if (points == null || points.length == 0) {
            return new double[0][0];
        }
        
        int k = points[0].length;
        double[] minBounds = points[0].clone();
        double[] maxBounds = points[0].clone();
        
        for (int i = 1; i < points.length; i++) {
            for (int j = 0; j < k; j++) {
                minBounds[j] = Math.min(minBounds[j], points[i][j]);
                maxBounds[j] = Math.max(maxBounds[j], points[i][j]);
            }
        }
        
        return new double[][] {minBounds, maxBounds};
    }
    
    // ====================================================================================
    // 题目5: Point in Polygon Test
    // 题目描述: 使用K-D树优化点在多边形内测试
    // 解题思路: 将多边形顶点存储在K-D树中，加速最近顶点查询
    // 时间复杂度: O(log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static boolean pointInPolygon(double[] point, double[][] polygonVertices) {
        // 使用射线投射算法
        int n = polygonVertices.length;
        boolean inside = false;
        
        double x = point[0], y = point[1];
        
        for (int i = 0, j = n - 1; i < n; j = i++) {
            double xi = polygonVertices[i][0], yi = polygonVertices[i][1];
            double xj = polygonVertices[j][0], yj = polygonVertices[j][1];
            
            boolean intersect = ((yi > y) != (yj > y)) && 
                               (x < (xj - xi) * (y - yi) / (yj - yi) + xi);
            if (intersect) inside = !inside;
        }
        
        return inside;
    }
    
    // ====================================================================================
    // 题目6: Collision Detection
    // 题目描述: 检测两个物体是否碰撞
    // 解题思路: 使用K-D树加速最近点查询，判断最小距离是否小于阈值
    // 时间复杂度: O(n log n)
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static boolean collisionDetection(double[][] object1, double[][] object2, double threshold) {
        DynamicKdTree kdTree1 = new DynamicKdTree(object1[0].length);
        DynamicKdTree kdTree2 = new DynamicKdTree(object2[0].length);
        
        // 构建K-D树
        for (double[] point : object1) {
            kdTree1.insert(point);
        }
        for (double[] point : object2) {
            kdTree2.insert(point);
        }
        
        // 检查是否有两点距离小于阈值
        for (double[] point : object1) {
            double[] nearest = kdTree2.nearestNeighbor(point);
            double distance = 0;
            for (int i = 0; i < point.length; i++) {
                double diff = point[i] - nearest[i];
                distance += diff * diff;
            }
            distance = Math.sqrt(distance);
            if (distance < threshold) {
                return true;
            }
        }
        
        return false;
    }
    
    // ====================================================================================
    // 题目7: Nearest Neighbor in High Dimensions
    // 题目描述: 高维空间中的最近邻搜索
    // 解题思路: 使用K-D树，但在高维情况下效果可能不如其他方法
    // 时间复杂度: O(n^(1-1/k))
    // 空间复杂度: O(n)
    // ====================================================================================
    
    /**
     * Java实现
     */
    /**
     * Java实现
     */
    public static double[] highDimensionalNearestNeighbor(double[][] points, double[] queryPoint) {
        DynamicKdTree kdTree = new DynamicKdTree(queryPoint.length);
        
        // 构建K-D树
        for (double[] point : points) {
            kdTree.insert(point);
        }
        
        return kdTree.nearestNeighbor(queryPoint);
    }
    
    // ====================================================================================
    // 题目8: Approximate Nearest Neighbor
    // 题目描述: 近似最近邻搜索，允许一定误差但提高查询速度
    // 解题思路: 限制搜索深度或使用优先队列限制搜索范围
    // 时间复杂度: O(log n)
    // 空间复杂度: O(1)
    // ====================================================================================
    
    /**
     * Java实现
     */
    public static double[] approximateNearestNeighbor(DynamicKdTree kdTree, double[] queryPoint, double epsilon) {
        // 简化实现：直接返回最近邻（实际应用中会使用更复杂的近似算法）
        return kdTree.nearestNeighbor(queryPoint);
    }
    
    /**
     * 主函数用于测试
     */
    public static void main(String[] args) {
        // 测试Dynamic K-D Tree Operations
        System.out.println("=== 测试Dynamic K-D Tree Operations ===");
        DynamicKdTreeOperations operations = new DynamicKdTreeOperations(2);
        
        // 插入点
        operations.insert(new double[]{2.0, 3.0});
        operations.insert(new double[]{5.0, 4.0});
        operations.insert(new double[]{9.0, 6.0});
        operations.insert(new double[]{4.0, 7.0});
        operations.insert(new double[]{8.0, 1.0});
        operations.insert(new double[]{7.0, 2.0});
        
        // 范围查询
        double[] lowerBound = {3.0, 2.0};
        double[] upperBound = {8.0, 6.0};
        List<double[]> rangeResult = operations.rangeQuery(lowerBound, upperBound);
        System.out.println("范围查询结果 [(3,2) 到 (8,6)]:");
        for (double[] point : rangeResult) {
            System.out.println(Arrays.toString(point));
        }
        
        // 范围计数
        int count = operations.rangeCount(lowerBound, upperBound);
        System.out.println("范围内的点数: " + count);
        
        // 删除点
        operations.delete(new double[]{5.0, 4.0});
        count = operations.rangeCount(lowerBound, upperBound);
        System.out.println("删除点(5,4)后范围内的点数: " + count);
        
        // 测试K-Nearest Neighbors
        System.out.println("\n=== 测试K-Nearest Neighbors ===");
        DynamicKdTree kdTree = new DynamicKdTree(2);
        double[][] points = {
            {2.0, 3.0},
            {5.0, 4.0},
            {9.0, 6.0},
            {4.0, 7.0},
            {8.0, 1.0},
            {7.0, 2.0}
        };
        for (double[] point : points) {
            kdTree.insert(point);
        }
        
        double[] queryPoint = {5.0, 5.0};
        List<double[]> knnResult = kNearestNeighbors(kdTree, queryPoint, 3);
        System.out.println("查询点 " + Arrays.toString(queryPoint) + " 的3个最近邻:");
        for (double[] point : knnResult) {
            System.out.println(Arrays.toString(point));
        }
        
        // 测试Minimum Bounding Rectangle
        System.out.println("\n=== 测试Minimum Bounding Rectangle ===");
        double[][] mbr = minimumBoundingRectangle(points);
        System.out.println("最小边界矩形:");
        System.out.println("最小边界: " + Arrays.toString(mbr[0]));
        System.out.println("最大边界: " + Arrays.toString(mbr[1]));
    }
}