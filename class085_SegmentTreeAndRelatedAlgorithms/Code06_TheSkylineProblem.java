package class112;

// 218. 天际线问题 - 线段树 + 离散化实现
// 题目来源：LeetCode 218 https://leetcode.cn/problems/the-skyline-problem/
// 
// 题目描述：
// 城市的 天际线 是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
// 给你所有建筑物的位置和高度，请返回 由这些建筑物形成的 天际线。
// 每个建筑物的几何信息由数组 buildings 表示，其中三元组 buildings[i] = [lefti, righti, heighti] 表示：
// lefti 是第 i 座建筑物左边缘的 x 坐标。
// righti 是第 i 座建筑物右边缘的 x 坐标。
// heighti 是第 i 座建筑物的高度。
// 你可以假设所有的建筑都是完美的长方形，在高度为 0 的绝对平坦的表面上。
// 天际线 应该表示为由 "关键点" 组成的列表，格式 [[x1,y1],[x2,y2],...] ，并按 x 坐标 进行 排序。
// 关键点是水平线段的左端点。列表中最后一个点是最右侧建筑物的终点，y 坐标始终为 0 ，仅用于标记天际线的终点。
// 此外，任何两个相邻建筑物之间的地面都应被视为天际线轮廓的一部分。
// 注意：输出天际线中不得有连续的相同高度的水平线。
// 
// 解题思路：
// 使用线段树配合离散化来解决天际线问题
// 1. 收集所有建筑物的左右边界坐标作为关键点
// 2. 对关键点进行离散化处理，建立坐标映射关系
// 3. 使用线段树维护区间最大高度，支持区间更新和单点查询
// 4. 遍历所有建筑物，将每个建筑物的高度更新到对应区间
// 5. 遍历所有离散化后的坐标点，查询高度变化的关键点
// 
// 核心思想：
// 1. 离散化：由于建筑物的坐标可能很大，直接使用原始坐标会导致空间浪费。
//    通过离散化将大范围的坐标映射到较小的连续整数范围，提高效率。
// 2. 线段树：用于维护区间最大高度信息，支持高效的区间更新和查询操作。
// 3. 懒惰传播：在区间更新时使用懒惰标记，避免不必要的重复计算。
// 4. 关键点识别：通过比较相邻点的高度变化来识别天际线的关键点。
// 
// 时间复杂度分析：
// - 收集关键点：O(n)
// - 离散化：O(n log n)
// - 构建线段树：O(n)
// - 处理建筑物：O(n log n)
// - 查询结果：O(n log n)
// - 总时间复杂度：O(n log n)
// 空间复杂度：O(n)
//
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.util.*;

public class Code06_TheSkylineProblem {
    
    // 使用线段树和离散化解决天际线问题
    static class Solution {
        // 线段树节点
        static class Node {
            int left, right;  // 区间左右端点
            int cover;        // 覆盖情况（懒惰标记）
            int max;          // 区间最大高度
            
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
        int cnt = 1;                   // 节点编号计数器
        Map<Integer, Integer> map = new HashMap<>();    // 离散化映射：实际坐标 -> 索引
        Map<Integer, Integer> revMap = new HashMap<>(); // 反向映射：索引 -> 实际坐标
        List<Integer> list = new ArrayList<>();         // 存储所有关键x坐标
        
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
         * 区间修改：将区间[L,R]的高度更新为h（取最大值）
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
                tr[u].cover = Math.max(tr[u].cover, h);
                tr[u].max = Math.max(tr[u].max, h);
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
         * 主函数：计算天际线
         * 通过离散化和线段树来高效解决天际线问题
         * @param buildings 建筑物信息数组，每个元素为[left, right, height]
         * @return 天际线关键点列表
         * 
         * 时间复杂度: O(n log n)
         * 空间复杂度: O(n)
         */
        public List<List<Integer>> getSkyline(int[][] buildings) {
            // 第一步：收集所有关键x坐标（建筑物的左右边界）
            // 为了处理边界情况，我们还需要添加右边界前一个位置的坐标
            for (int[] b : buildings) {
                list.add(b[0]);      // 左边界
                list.add(b[1]);      // 右边界
                list.add(b[1] - 1);  // 添加右端点前一个位置，用于处理边界情况
            }
            
            // 第二步：去重并排序
            // 使用HashSet去重，然后转换为ArrayList并排序
            list = new ArrayList<>(new HashSet<>(list));
            Collections.sort(list);
            
            // 第三步：建立离散化映射
            // 将实际坐标映射到连续的整数索引，便于线段树处理
            for (int i = 0; i < list.size(); i++) {
                map.put(list.get(i), i + 1);    // 实际坐标 -> 索引
                revMap.put(i + 1, list.get(i)); // 索引 -> 实际坐标
            }
            
            // 第四步：构建线段树
            // 初始化线段树，维护区间最大高度信息
            build(1, 1, list.size());
            
            // 第五步：处理每个建筑物，将高度更新到对应区间
            // 遍历所有建筑物，将每个建筑物的高度更新到对应的离散化区间
            for (int[] b : buildings) {
                // 将建筑物的左右边界映射到离散化后的索引
                int l = map.get(b[0]);      // 左边界映射
                int r = map.get(b[1] - 1);  // 右边界前一个位置映射
                // 更新区间高度，取最大值
                update(1, l, r, b[2]);      // 更新区间高度
            }
            
            // 第六步：收集结果
            // 遍历所有离散化后的坐标点，查询高度变化的关键点
            List<List<Integer>> res = new ArrayList<>();
            int pre = 0;  // 上一个高度，初始为0表示地面高度
            
            // 遍历所有离散化后的坐标点
            for (int i = 1; i <= list.size(); i++) {
                // 查询当前点的高度（即该点的最大建筑物高度）
                int h = query(1, i, i);  // 查询当前点的高度
                // 获取实际坐标
                int x = revMap.get(i);   // 获取实际坐标
                
                // 如果高度发生变化，则记录关键点
                // 只有当当前高度与上一个高度不同时，才记录为关键点
                if (h != pre) {
                    List<Integer> point = new ArrayList<>();
                    point.add(x);   // 添加x坐标
                    point.add(h);   // 添加y坐标（高度）
                    res.add(point); // 添加到结果列表
                    pre = h;        // 更新上一个高度
                }
            }
            
            return res;
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例: buildings = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
        // 期望输出: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
        int[][] buildings = {{2,9,10},{3,7,15},{5,12,12},{15,20,10},{19,24,8}};
        List<List<Integer>> result = solution.getSkyline(buildings);
        System.out.println(result);
    }
}