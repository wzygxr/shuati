// 灌溉花园的最少水龙头数目
// 在x轴上有一个一维的花园，花园长度为n，从点0开始，到点n结束
// 花园里总共有 n + 1 个水龙头，分别位于[0, 1, ... n]
// 给你一个整数n和一个长度为n+1的整数数组ranges
// 其中ranges[i]表示
// 如果打开点i处的水龙头，可以灌溉的区域为[i-ranges[i], i+ranges[i]]
// 请你返回可以灌溉整个花园的最少水龙头数目
// 如果花园始终存在无法灌溉到的地方请你返回-1
// 测试链接 : https://leetcode.cn/problems/minimum-number-of-taps-to-open-to-water-a-garden/

#define MAXN 100001

/**
 * 灌溉花园的最少水龙头数目 - 使用贪心算法解决
 * 
 * 算法思路：
 * 这是一个经典的区间覆盖问题，可以转化为跳跃游戏的变种。
 * 1. 首先预处理ranges数组，构造right数组，其中right[i]表示以位置i为起点，
 *    能够覆盖到的最远右边界。
 * 2. 使用贪心策略，维护两个变量：
 *    - cur: 当前水龙头能覆盖到的最远位置
 *    - next: 下一个水龙头能覆盖到的最远位置
 *    - ans: 打开水龙头的数量
 * 3. 遍历位置0到n-1，当当前位置超过当前水龙头能覆盖的范围时，
 *    就需要打开下一个水龙头，并更新相关变量。
 * 
 * 时间复杂度：O(n) - 只需遍历数组一次
 * 空间复杂度：O(n) - 需要额外的right数组
 * 
 * 是否最优解：是。这是该问题的最优解法之一。
 * 
 * 适用场景：
 * 1. 区间覆盖问题
 * 2. 最少资源选择问题
 * 
 * 相关题目：
 * 1. LeetCode 45. 跳跃游戏 II - 经典跳跃游戏
 * 2. LeetCode 55. 跳跃游戏 - 判断是否能到达终点
 * 3. LeetCode 1024. 视频拼接 - 区间拼接问题
 * 4. LeetCode 1326. 灌溉花园的最少水龙头数目 - 与本题相同
 * 5. 牛客网 NC135 买票需要多少时间 - 队列模拟相关
 * 6. LintCode 391. 数飞机 - 区间调度相关
 * 7. HackerRank - Jim and the Orders - 贪心调度问题
 * 8. CodeChef - TACHSTCK - 区间配对问题
 * 9. AtCoder ABC104C - All Green - 动态规划相关
 * 10. Codeforces 1363C - Game On Leaves - 博弈论相关
 * 11. SPOJ ANARC08E - Relax! I am a legend - 数学相关
 * 12. POJ 3169 - Layout - 差分约束系统
 * 13. HDU 2586 - How far away? - LCA最近公共祖先
 * 14. USACO 2014 January Silver - Cross Country Skiing - BFS搜索
 * 15. 洛谷 P1091 - 合唱队形 - 动态规划最长子序列
 * 16. Project Euler 357 - Prime generating integers - 数论相关
 * 17. 洛谷 P1208 - 混合牛奶 - 经典贪心问题
 * 18. 牛客网 NC140 - 排序 - 各种排序算法实现
 */
int minTaps(int n, int ranges[]) {
    // right[i] = j
    // 所有左边界在i的水龙头里，影响到的最右右边界是j
    int right[MAXN] = {0};
    for (int i = 0, start; i <= n; i++) {
        start = i - ranges[i];
        if (start < 0) start = 0;
        int end = i + ranges[i];
        if (end > right[start]) {
            right[start] = end;
        }
    }
    // 当前ans数量的水龙头打开，影响到的最右右边界
    int cur = 0;
    // 如果再多打开一个水龙头，影响到的最右边界
    int next = 0;
    // 打开水龙头的数量
    int ans = 0;
    for (int i = 0; i < n; i++) {
        // 来到i位置
        // 先更新下一步的next
        if (next < right[i]) {
            next = right[i];
        }
        if (i == cur) {
            if (next > i) {
                cur = next;
                ans++;
            } else {
                return -1;
            }
        }
    }
    return ans;
}

// 测试函数，返回结果用于验证
int test_minTaps() {
    // 测试用例1: 基本情况
    int n1 = 5;
    int ranges1[] = {3, 4, 1, 1, 0, 0};
    int result1 = minTaps(n1, ranges1);
    
    // 测试用例2: 需要多个水龙头
    int n2 = 3;
    int ranges2[] = {0, 0, 0, 0};
    int result2 = minTaps(n2, ranges2);
    
    // 测试用例3: 精确覆盖
    int n3 = 7;
    int ranges3[] = {1, 2, 1, 0, 2, 1, 0, 1};
    int result3 = minTaps(n3, ranges3);
    
    // 测试用例4: 单个水龙头覆盖全部
    int n4 = 8;
    int ranges4[] = {4, 0, 0, 0, 0, 0, 0, 0, 4};
    int result4 = minTaps(n4, ranges4);
    
    // 返回最后一个测试用例的结果
    return result4;
}

// 主函数，用于编译和运行测试
int main() {
    return test_minTaps();
}