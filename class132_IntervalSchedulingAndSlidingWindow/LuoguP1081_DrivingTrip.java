package class129;

import java.util.*;

/**
 * 洛谷 P1081 开车旅行
 * 
 * 题目描述：
 * 小 A 和小 B 决定利用假期外出旅行，他们将想去的城市从 1 到 n 编号，且编号较小的城市在编号较大的城市的西边，
 * 已知各个城市的海拔高度互不相同，记城市 i 的海拔高度为hi，城市 i 和城市 j 之间的距离 di,j 恰好是这两个城市海拔高度之差的绝对值，
 * 即 di,j = |hi - hj|。
 * 旅行过程中，小 A 和小 B 轮流开车，第一天小 A 开车，之后每天轮换一次。他们计划选择一个城市 s 作为起点，
 * 一直向东行驶，并且最多行驶 x 公里就结束旅行。
 * 小 B 总是沿着前进方向选择一个最近的城市作为目的地，而小 A 总是沿着前进方向选择第二近的城市作为目的地
 * （注意：本题中如果当前城市到两个城市的距离相同，则认为离海拔低的那个城市更近）。
 * 如果其中任何一人无法按照自己的原则选择目的城市，或者到达目的地会使行驶的总距离超出 x 公里，他们就会结束旅行。
 * 
 * 问题1 : 给定距离x0，返回1 ~ n-1中从哪个点出发，a行驶距离 / b行驶距离，比值最小
 *         如果从多个点出发时，比值都为最小，那么返回arr中的值最大的点
 * 问题2 : 给定s、x，返回旅行停止时，a开了多少距离、b开了多少距离
 * 
 * 解题思路：
 * 这是一个结合了数据结构和倍增思想的复杂问题。
 * 
 * 核心思想：
 * 1. 预处理：对于每个城市，找到它右边的第一近和第二近城市
 * 2. 倍增优化：预处理2^k轮a和b交替开车能到达的位置和距离
 * 3. 查询处理：使用倍增快速计算任意起点和距离限制下的行驶情况
 * 
 * 具体步骤：
 * 1. 使用TreeSet或双向链表找到每个城市的第一近和第二近城市
 * 2. 使用倍增思想预处理状态转移表
 * 3. 对于查询，使用倍增快速计算结果
 * 
 * 时间复杂度：预处理O(n log n)，查询O(log x)
 * 空间复杂度：O(n log n)
 * 
 * 相关题目：
 * - LeetCode 220. 存在重复元素 III (TreeSet应用)
 * - POJ 1733 - Parity game (离散化 + 倍增)
 * - Codeforces 822D - My pretty girl Noora (数学 + 倍增)
 */
public class LuoguP1081_DrivingTrip {
    
    // 最大节点数
    public static final int MAXN = 100002;
    // 最大幂次
    public static final int MAXP = 20;
    
    // 城市海拔数组
    public static int[] heights = new int[MAXN];
    
    // to1[i]: i城市右侧第一近城市编号
    public static int[] to1 = new int[MAXN];
    // dist1[i]: i城市到第一近城市的距离
    public static int[] dist1 = new int[MAXN];
    // to2[i]: i城市右侧第二近城市编号
    public static int[] to2 = new int[MAXN];
    // dist2[i]: i城市到第二近城市的距离
    public static int[] dist2 = new int[MAXN];
    
    // stto[i][p]: 从i位置出发，a和b轮流开2^p轮之后，车到达了几号点
    public static int[][] stto = new int[MAXN][MAXP + 1];
    // stdist[i][p]: 从i位置出发，a和b轮流开2^p轮之后，总距离是多少
    public static int[][] stdist = new int[MAXN][MAXP + 1];
    // sta[i][p]: 从i位置出发，a和b轮流开2^p轮之后，a行驶了多少距离
    public static int[][] sta = new int[MAXN][MAXP + 1];
    // stb[i][p]: 从i位置出发，a和b轮流开2^p轮之后，b行驶了多少距离
    public static int[][] stb = new int[MAXN][MAXP + 1];
    
    public static int n;
    
    /**
     * 预处理每个城市的第一近和第二近城市
     */
    public static void preprocessNear() {
        // 创建城市信息数组并按海拔排序
        int[][] cities = new int[n + 1][2];
        for (int i = 1; i <= n; i++) {
            cities[i][0] = i;  // 城市编号
            cities[i][1] = heights[i];  // 城市海拔
        }
        
        // 按海拔排序
        Arrays.sort(cities, 1, n + 1, (a, b) -> a[1] - b[1]);
        
        // 建立双向链表
        int[] prev = new int[MAXN];
        int[] next = new int[MAXN];
        
        cities[0][0] = 0;
        cities[n + 1][0] = 0;
        
        for (int i = 1; i <= n; i++) {
            prev[cities[i][0]] = cities[i - 1][0];
            next[cities[i][0]] = cities[i + 1][0];
        }
        
        // 从编号小到大处理每个城市
        int[][] tempCities = new int[n + 1][2];
        for (int i = 1; i <= n; i++) {
            tempCities[i][0] = i;
            tempCities[i][1] = heights[i];
        }
        
        // 按编号排序
        Arrays.sort(tempCities, 1, n + 1, (a, b) -> a[0] - b[0]);
        
        // 对每个城市找第一近和第二近
        for (int i = 1; i <= n; i++) {
            int city = tempCities[i][0];
            to1[city] = 0;
            dist1[city] = 0;
            to2[city] = 0;
            dist2[city] = 0;
            
            // 在链表中找到当前城市
            int pos = 0;
            for (int j = 1; j <= n; j++) {
                if (cities[j][0] == city) {
                    pos = j;
                    break;
                }
            }
            
            // 检查附近的4个城市
            if (pos > 1) {
                update(city, cities[pos - 1][0]);
            }
            if (pos > 2) {
                update(city, cities[pos - 2][0]);
            }
            if (pos < n) {
                update(city, cities[pos + 1][0]);
            }
            if (pos < n - 1) {
                update(city, cities[pos + 2][0]);
            }
        }
    }
    
    /**
     * 更新城市i的最近和次近城市信息
     * 
     * @param i 城市编号
     * @param j 可能的最近或次近城市编号
     */
    public static void update(int i, int j) {
        if (j == 0) {
            return;
        }
        
        int dist = Math.abs(heights[i] - heights[j]);
        if (to1[i] == 0 || dist < dist1[i] || (dist == dist1[i] && heights[j] < heights[to1[i]])) {
            to2[i] = to1[i];
            dist2[i] = dist1[i];
            to1[i] = j;
            dist1[i] = dist;
        } else if (to2[i] == 0 || dist < dist2[i] || (dist == dist2[i] && heights[j] < heights[to2[i]])) {
            to2[i] = j;
            dist2[i] = dist;
        }
    }
    
    /**
     * 倍增预处理
     */
    public static void preprocessST() {
        // 倍增初始化
        for (int i = 1; i <= n; i++) {
            // 一轮：a开到第二近，b开到第一近
            stto[i][0] = to1[to2[i]];  // 从i出发，a开到to2[i]，b再开到to1[to2[i]]
            if (stto[i][0] != 0) {
                stdist[i][0] = dist2[i] + dist1[to2[i]];  // 总距离
                sta[i][0] = dist2[i];  // a行驶距离
                stb[i][0] = dist1[to2[i]];  // b行驶距离
            }
        }
        
        // 生成倍增表
        for (int p = 1; p <= MAXP; p++) {
            for (int i = 1; i <= n; i++) {
                stto[i][p] = stto[stto[i][p - 1]][p - 1];
                if (stto[i][p] != 0) {
                    stdist[i][p] = stdist[i][p - 1] + stdist[stto[i][p - 1]][p - 1];
                    sta[i][p] = sta[i][p - 1] + sta[stto[i][p - 1]][p - 1];
                    stb[i][p] = stb[i][p - 1] + stb[stto[i][p - 1]][p - 1];
                }
            }
        }
    }
    
    /**
     * 计算从城市s出发，最多行驶x距离时，a和b各自行驶的距离
     * 
     * @param s 起始城市
     * @param x 最大行驶距离
     * @param result 结果数组，result[0]为a行驶距离，result[1]为b行驶距离
     */
    public static void travel(int s, int x, int[] result) {
        int aDist = 0, bDist = 0;
        
        // 使用倍增快速计算
        for (int p = MAXP; p >= 0; p--) {
            if (stto[s][p] != 0 && x >= stdist[s][p]) {
                x -= stdist[s][p];
                aDist += sta[s][p];
                bDist += stb[s][p];
                s = stto[s][p];
            }
        }
        
        // 处理最后一步（如果a还能开）
        if (dist2[s] <= x) {
            aDist += dist2[s];
        }
        
        result[0] = aDist;
        result[1] = bDist;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 模拟测试用例
        n = 4;
        heights[1] = 10;
        heights[2] = 20;
        heights[3] = 15;
        heights[4] = 30;
        
        System.out.println("测试用例:");
        System.out.println("城市数: " + n);
        System.out.print("各城市海拔: ");
        for (int i = 1; i <= n; i++) {
            System.out.print(heights[i] + " ");
        }
        System.out.println();
        
        // 预处理
        preprocessNear();
        preprocessST();
        
        // 查询示例
        int[] result = new int[2];
        travel(1, 25, result);
        System.out.println("从城市1出发，最多行驶25距离:");
        System.out.println("a行驶距离: " + result[0] + ", b行驶距离: " + result[1]);
    }
}