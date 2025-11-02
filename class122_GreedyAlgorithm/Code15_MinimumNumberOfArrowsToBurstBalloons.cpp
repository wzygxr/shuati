// 用最少数量的箭引爆气球
// 一些球形的气球贴在一堵用 XY 平面表示的墙面上。墙面上的气球记录在整数数组 points，
// 其中 points[i] = [xstart, xend] 表示水平直径在 xstart 和 xend 之间的气球。
// 你不知道气球的确切 y 坐标。
// 一支弓箭可以沿着 x 轴从不同点完全垂直地射出。在坐标 x 处射出一支箭，
// 若有一个气球的直径的开始和结束坐标为 xstart，xend，且满足 xstart ≤ x ≤ xend，
// 则该气球会被引爆。可以射出的弓箭的数量没有限制。弓箭一旦被射出之后，可以无限地前进。
// 给你一个数组 points，返回引爆所有气球所必须射出的最小弓箭数。
// 测试链接 : https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/

/**
 * 简单排序函数（按气球结束位置排序）
 * 
 * @param points 气球坐标数组
 * @param pointsSize 数组长度
 */
void sortBalloons(int points[][2], int pointsSize) {
    for (int i = 0; i < pointsSize - 1; i++) {
        for (int j = 0; j < pointsSize - i - 1; j++) {
            if (points[j][1] > points[j + 1][1]) {
                // 交换气球坐标
                int temp0 = points[j][0];
                int temp1 = points[j][1];
                points[j][0] = points[j + 1][0];
                points[j][1] = points[j + 1][1];
                points[j + 1][0] = temp0;
                points[j + 1][1] = temp1;
            }
        }
    }
}

/**
 * 用最少数量的箭引爆气球
 * 
 * 算法思路：
 * 使用贪心策略：
 * 1. 按照气球的结束位置进行升序排序
 * 2. 贪心选择结束位置最早的气球，射出一支箭
 * 3. 这支箭能引爆所有与该气球重叠的气球
 * 4. 继续处理未被引爆的气球
 * 
 * 正确性分析：
 * 1. 为了使用最少的箭，我们应该尽可能多地引爆气球
 * 2. 按结束位置排序后，选择结束位置最早的气球射箭
 * 3. 这样可以保证与该气球重叠的所有气球都被引爆
 * 
 * 时间复杂度：O(n^2) - 使用冒泡排序
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * @param points 气球坐标数组
 * @param pointsSize 数组长度
 * @return 最少弓箭数
 */
int findMinArrowShots(int points[][2], int pointsSize) {
    // 边界情况处理
    if (pointsSize == 0) {
        return 0;
    }
    
    // 按照气球的结束位置进行升序排序
    sortBalloons(points, pointsSize);
    
    int arrows = 1;           // 至少需要一支箭
    int end = points[0][1];   // 第一支箭的位置
    
    // 从第二个气球开始遍历
    for (int i = 1; i < pointsSize; i++) {
        // 如果当前气球的开始位置大于箭的位置，说明需要新的箭
        if (points[i][0] > end) {
            arrows++;
            end = points[i][1];
        }
        // 否则当前箭可以引爆这个气球，不需要额外操作
    }
    
    return arrows;
}