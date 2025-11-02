package class112;

// 699. 掉落的方块 - 线段树实现
// 题目来源：LeetCode 699 https://leetcode.cn/problems/falling-squares/
// 
// 题目描述：
// 在二维平面上的 x 轴上，放置着一些方块。
// 给你一个二维整数数组 positions ，其中 positions[i] = [lefti, sideLengthi] 表示：
// 第 i 个方块边长为 sideLengthi ，其左侧边与 x 轴上坐标点 lefti 对齐。
// 每个方块都从一个比目前所有的落地方块更高的高度掉落而下。
// 方块沿 y 轴负方向下落，直到着陆到 另一个正方形的顶边 或者是 x 轴上。
// 一个方块仅仅是擦过另一个方块的左侧边或右侧边不算着陆。
// 一旦着陆，它就会固定在原地，无法移动。
// 在每个方块掉落后，你必须记录目前所有已经落稳的 方块堆叠的最高高度。
// 返回一个整数数组 ans ，其中 ans[i] 表示在第 i 块方块掉落后堆叠的最高高度。
// 
// 解题思路：
// 使用线段树配合离散化来解决掉落方块问题
// 1. 收集所有方块的左右边界坐标作为关键点
// 2. 对关键点进行离散化处理，建立坐标映射关系
// 3. 使用线段树维护区间最大高度，支持区间更新和区间查询
// 4. 对于每个掉落的方块，先查询其底部区间当前的最大高度，然后将整个区间更新为新高度
// 5. 记录每次掉落后的全局最大高度
// 
// 核心思想：
// 1. 离散化：由于方块的坐标可能很大，直接使用原始坐标会导致空间浪费。
//    通过离散化将大范围的坐标映射到较小的连续整数范围，提高效率。
// 2. 线段树：用于维护区间最大高度信息，支持高效的区间更新和查询操作。
// 3. 懒惰传播：在区间更新时使用懒惰标记，避免不必要的重复计算。
// 4. 着陆高度计算：新方块的着陆高度等于其底部区间当前的最大高度。
// 5. 堆叠高度计算：新方块的堆叠高度 = 着陆高度 + 方块高度。
// 
// 时间复杂度分析：
// - 收集关键点：O(n)
// - 离散化：O(n log n)
// - 构建线段树：O(n)
// - 处理方块：O(n log n)
// - 总时间复杂度：O(n log n)
// 空间复杂度：O(n)
//
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.util.*;

public class Code07_FallingSquares {
    
    // 使用线段树解决掉落的方块问题
    static class Solution {
        // 线段树节点
        static class Node {
            int left, right;     // 区间左右端点
            int max;             // 区间最大高度
            int cover;           // 懒惰标记：区间覆盖的高度
            
            /**
             * 构造函数，创建线段树节点
             * @param l 区间左端点
             * @param r 区间右端点
             */
            Node(int l, int r) {
                left = l;
                right = r;
            }
        }
        
        Node[] tr = new Node[400010];  // 线段树数组
        
        /**
         * 上推操作，更新父节点信息
         * 将左右子节点的最大值更新到父节点
         * 在线段树中，父节点的值通常由子节点的值计算得出
         * 对于本问题，父节点维护的区间最大高度等于左右子节点维护区间最大高度的最大值
         * 
         * 时间复杂度: O(1)
         */
        void pushup(int u) {
            // 更新当前节点的最大高度为左右子节点最大高度的最大值
            tr[u].max = Math.max(tr[u << 1].max, tr[u << 1 | 1].max);
        }
        
        /**
         * 下传操作，传递懒惰标记
         * 将当前节点的懒惰标记传递给左右子节点
         * 懒惰传播是线段树优化的重要技术，用于延迟更新操作
         * 只有在真正需要访问子节点时才将更新操作传递下去，避免不必要的计算
         * 
         * 时间复杂度: O(1)
         */
        void pushdown(int u) {
            // 只有当当前节点有懒惰标记时才需要下传
            if (tr[u].cover != 0) {
                // 更新左子节点的懒惰标记和最大值
                // 将当前节点的覆盖高度传递给左子节点
                tr[u << 1].cover = tr[u].cover;
                // 左子节点的最大高度更新为覆盖高度
                tr[u << 1].max = tr[u].cover;
                
                // 更新右子节点的懒惰标记和最大值
                // 将当前节点的覆盖高度传递给右子节点
                tr[u << 1 | 1].cover = tr[u].cover;
                // 右子节点的最大高度更新为覆盖高度
                tr[u << 1 | 1].max = tr[u].cover;
                
                // 清除当前节点的懒惰标记
                // 标记已传递，当前节点的懒惰标记清零
                tr[u].cover = 0;
            }
        }
        
        /**
         * 构建线段树
         * 采用递归方式构建线段树，每个节点维护一个区间的信息
         * 叶子节点对应数组中的单个元素，非叶子节点对应区间的合并结果
         * @param u 当前节点索引
         * @param l 区间左边界
         * @param r 区间右边界
         * 
         * 时间复杂度: O(n)
         */
        void build(int u, int l, int r) {
            // 创建当前节点并设置其维护的区间范围
            tr[u] = new Node(l, r);
            // 叶子节点不需要继续构建子树
            if (l == r) return;
            // 计算中点，将区间分为两部分
            int mid = l + r >> 1;
            // 递归构建左子树
            build(u << 1, l, mid);
            // 递归构建右子树
            build(u << 1 | 1, mid + 1, r);
        }
        
        /**
         * 区间修改：将区间[L,R]的高度更新为h（覆盖）
         * 利用懒惰传播优化，避免对每个元素逐一更新
         * @param u 当前节点索引
         * @param L 操作区间左边界
         * @param R 操作区间右边界
         * @param h 要更新的高度
         * 
         * 时间复杂度: O(log n)
         */
        void update(int u, int L, int R, int h) {
            // 优化1：如果当前节点维护的区间完全被操作区间覆盖
            if (L <= tr[u].left && tr[u].right <= R) {
                // 当前区间完全被操作区间覆盖，更新懒惰标记和最大值
                // 这是懒惰传播的关键：只标记不立即执行
                tr[u].cover = h;
                tr[u].max = h;
                return;
            }
            
            // 下传懒惰标记
            // 在递归处理子节点之前，需要确保当前节点的懒惰标记已经传递
            pushdown(u);
            
            // 计算中点，将区间分为两部分
            int mid = tr[u].left + tr[u].right >> 1;
            // 递归更新左子树
            // 只有当操作区间与左子树区间有交集时才继续处理
            if (L <= mid) update(u << 1, L, R, h);
            // 递归更新右子树
            // 只有当操作区间与右子树区间有交集时才继续处理
            if (R > mid) update(u << 1 | 1, L, R, h);
            
            // 上推更新父节点
            // 将子节点的更新结果合并到当前节点
            pushup(u);
        }
        
        /**
         * 查询区间最大值
         * 在查询过程中需要确保懒惰标记已经正确传递
         * @param u 当前节点索引
         * @param L 查询区间左边界
         * @param R 查询区间右边界
         * @return 区间[L,R]内的最大值
         * 
         * 时间复杂度: O(log n)
         */
        int query(int u, int L, int R) {
            // 优化1：如果当前节点维护的区间完全包含在查询区间内
            if (L <= tr[u].left && tr[u].right <= R) {
                // 当前区间完全包含在查询区间内，直接返回最大值
                // 这是线段树查询的优化点：如果当前区间完全在查询区间内，直接返回结果
                return tr[u].max;
            }
            
            // 下传懒惰标记
            // 在查询时必须确保懒惰标记已经传递，以保证结果正确
            pushdown(u);
            
            // 计算中点，将区间分为两部分
            int mid = tr[u].left + tr[u].right >> 1;
            int res = 0;
            // 递归查询左子树
            // 只有当查询区间与左子树区间有交集时才继续查询
            if (L <= mid) res = Math.max(res, query(u << 1, L, R));
            // 递归查询右子树
            // 只有当查询区间与右子树区间有交集时才继续查询
            if (R > mid) res = Math.max(res, query(u << 1 | 1, L, R));
            return res;
        }
        
        /**
         * 主函数：计算掉落方块后的最大高度
         * 通过离散化和线段树来高效解决掉落方块问题
         * @param positions 方块位置信息数组，每个元素为[left, sideLength]
         * @return 每个方块掉落后堆叠的最高高度列表
         * 
         * 时间复杂度: O(n log n)
         * 空间复杂度: O(n)
         */
        public List<Integer> fallingSquares(int[][] positions) {
            // 第一步：收集所有关键x坐标（方块的左右边界）
            List<Integer> list = new ArrayList<>();
            for (int[] pos : positions) {
                list.add(pos[0]);              // 左边界
                list.add(pos[0] + pos[1] - 1); // 右边界
            }
            
            // 第二步：去重并排序
            // 使用HashSet去重，然后转换为ArrayList并排序
            list = new ArrayList<>(new HashSet<>(list));
            Collections.sort(list);
            
            // 第三步：建立离散化映射：实际坐标 -> 索引
            // 将实际坐标映射到连续的整数索引，便于线段树处理
            Map<Integer, Integer> map = new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                map.put(list.get(i), i + 1);
            }
            
            // 第四步：构建线段树
            // 初始化线段树，维护区间最大高度信息
            build(1, 1, list.size());
            
            // 第五步：处理每个方块并收集结果
            List<Integer> result = new ArrayList<>();
            int maxHeight = 0;  // 全局最大高度
            
            // 处理每个方块
            for (int[] pos : positions) {
                // 计算方块的左右边界
                int left = pos[0];                    // 方块左边界
                int size = pos[1];                    // 方块边长
                int right = left + size - 1;          // 方块右边界
                
                // 查询当前方块底部区间最大高度（即着陆高度）
                // 方块会落在其底部区间当前最大高度之上
                int currentHeight = query(1, map.get(left), map.get(right));
                // 新的高度 = 着陆高度 + 方块高度
                int newHeight = currentHeight + size;
                
                // 更新方块所在区间高度为新高度
                // 将方块覆盖的区间更新为新的堆叠高度
                update(1, map.get(left), map.get(right), newHeight);
                
                // 更新全局最大高度
                // 记录到目前为止所有方块堆叠的最大高度
                maxHeight = Math.max(maxHeight, newHeight);
                result.add(maxHeight);
            }
            
            return result;
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1: positions = [[1,2],[2,3],[6,1]]
        // 期望输出: [2,5,5]
        // 解释：第一个方块[1,2]高度为2，第二个方块[2,4]底部高度为2，总高度为5，第三个方块[6,6]底部高度为0，总高度为1
        int[][] positions1 = {{1,2},{2,3},{6,1}};
        System.out.println(solution.fallingSquares(positions1));
        
        // 测试用例2: positions = [[100,100],[200,100]]
        // 期望输出: [100,100]
        // 解释：两个方块不重叠，各自高度为100
        int[][] positions2 = {{100,100},{200,100}};
        System.out.println(solution.fallingSquares(positions2));
    }
}