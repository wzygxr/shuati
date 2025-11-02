package class170;

// P3755 [CQOI2017]老C的任务
// 平台: 洛谷
// 难度: 提高+/省选-
// 标签: CDQ分治, 二维数点
// 链接: https://www.luogu.com.cn/problem/P3755

// 题目描述:
// 老 C 是个程序员。
// 最近老 C 从老板那里接到了一个任务——给城市中的手机基站写个管理系统。
// 由于一个基站的面积相对于整个城市面积来说非常的小，因此每个的基站都可以看作坐标系中的一个点，
// 其位置可以用坐标 (x,y) 来表示。此外，每个基站还有很多属性，例如高度、功率等。
// 运营商经常会划定一个区域，并查询区域中所有基站的信息。
// 现在你需要实现的功能就是，对于一个给定的矩形区域，回答该区域中（包括区域边界上的）所有基站的功率总和。

// 解题思路:
// 这是一个二维数点问题，可以使用CDQ分治来解决。
// 将基站的插入操作和查询操作都看作事件，然后使用CDQ分治处理。
// 1. 将每个基站看作一个插入事件
// 2. 将每次查询拆分成四个前缀和查询
// 3. 按照x坐标排序
// 4. 使用CDQ分治处理，在合并过程中使用树状数组维护y坐标维度上的前缀和

import java.io.*;
import java.util.*;

public class P3755CQOI2017老C的任务 {
    static final int MAXN = 200005;
    
    // 事件类型：1-插入基站，2-查询
    static class Event {
        int type, x, y, power, id;  // type:1插入,2查询; power:插入时为功率，查询时为系数; id:查询编号
        Event(int type, int x, int y, int power, int id) {
            this.type = type;
            this.x = x;
            this.y = y;
            this.power = power;
            this.id = id;
        }
    }
    
    static Event[] events = new Event[MAXN];
    static long[] tree = new long[MAXN];  // 树状数组
    static long[] ans = new long[MAXN];   // 答案数组
    static int cnt = 0, n, m;
    
    static int lowbit(int x) {
        return x & (-x);
    }
    
    static void add(int pos, long val) {
        for (int i = pos; i < MAXN; i += lowbit(i)) {
            tree[i] += val;
        }
    }
    
    static long query(int pos) {
        long res = 0;
        for (int i = pos; i > 0; i -= lowbit(i)) {
            res += tree[i];
        }
        return res;
    }
    
    // 自定义比较器
    static class YComparator implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            return events[a].y - events[b].y;
        }
    }
    
    // 添加基站插入事件
    static void addBaseStation(int x, int y, int power) {
        events[++cnt] = new Event(1, x, y, power, 0);
    }
    
    // 添加查询事件，使用容斥原理将矩形查询转换为前缀和查询
    static void addQuery(int x, int y, int coeff, int id) {
        events[++cnt] = new Event(2, x, y, coeff, id);
    }
    
    // CDQ分治
    static void cdq(int l, int r) {
        if (l >= r) return;
        int mid = (l + r) >> 1;
        cdq(l, mid);
        cdq(mid + 1, r);
        
        // 处理左半部分对右半部分的贡献
        // 按y坐标排序
        Integer[] left = new Integer[mid - l + 1];
        Integer[] right = new Integer[r - mid];
        for (int i = l; i <= mid; i++) left[i - l] = i;
        for (int i = mid + 1; i <= r; i++) right[i - mid - 1] = i;
        
        // 按y坐标排序
        Arrays.sort(left, new YComparator());
        Arrays.sort(right, new YComparator());
        
        int j = 0;
        for (int i = 0; i < right.length; i++) {
            // 处理插入事件
            while (j < left.length && events[left[j]].y <= events[right[i]].y) {
                if (events[left[j]].type == 1) {
                    add(events[left[j]].x, events[left[j]].power);
                }
                j++;
            }
            // 处理查询事件
            if (events[right[i]].type == 2) {
                ans[events[right[i]].id] += (long)events[right[i]].power * query(events[right[i]].x);
            }
        }
        
        // 清空树状数组
        for (int i = 0; i < j; i++) {
            if (events[left[i]].type == 1) {
                add(events[left[i]].x, -events[left[i]].power);
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] nm = reader.readLine().split(" ");
        n = Integer.parseInt(nm[0]);
        m = Integer.parseInt(nm[1]);
        
        // 读取基站信息
        for (int i = 0; i < n; i++) {
            String[] line = reader.readLine().split(" ");
            int x = Integer.parseInt(line[0]);
            int y = Integer.parseInt(line[1]);
            int power = Integer.parseInt(line[2]);
            addBaseStation(x, y, power);
        }
        
        // 读取查询信息
        for (int i = 1; i <= m; i++) {
            String[] line = reader.readLine().split(" ");
            int x1 = Integer.parseInt(line[0]);
            int y1 = Integer.parseInt(line[1]);
            int x2 = Integer.parseInt(line[2]);
            int y2 = Integer.parseInt(line[3]);
            
            // 使用容斥原理将矩形区域查询转换为四个前缀和查询
            addQuery(x2, y2, 1, i);      // 右上角区域加
            addQuery(x1-1, y1-1, 1, i);  // 左下角区域加
            addQuery(x1-1, y2, -1, i);   // 左上角区域减
            addQuery(x2, y1-1, -1, i);   // 右下角区域减
        }
        
        // 按照x坐标排序
        Arrays.sort(events, 1, cnt + 1, (a, b) -> {
            if (a.x != b.x) return a.x - b.x;
            return a.type - b.type;  // 插入事件优先于查询事件
        });
        
        // CDQ分治求解
        cdq(1, cnt);
        
        // 输出答案
        for (int i = 1; i <= m; i++) {
            System.out.println(ans[i]);
        }
    }
}