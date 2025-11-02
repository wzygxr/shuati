// 扫描线问题 - 分块算法实现 (Java版本)
// 题目来源: HDU 1542
// 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1542
// 题目大意: 计算多个矩形覆盖的总面积
// 约束条件: 矩形数量n ≤ 100

import java.io.*;
import java.util.*;

class Line implements Comparable<Line> {
    double x, y1, y2;
    int flag; // 1表示进入，-1表示离开
    
    Line(double x, double y1, double y2, int flag) {
        this.x = x;
        this.y1 = y1;
        this.y2 = y2;
        this.flag = flag;
    }
    
    @Override
    public int compareTo(Line other) {
        return Double.compare(this.x, other.x);
    }
}

public class Code36_Scanline {
    static final int MAXN = 205;
    static Line[] lines;
    static double[] yCoords;
    static int n, m;
    
    // 分块数据结构
    static int blen; // 块的大小
    static int blockCount; // 块的数量
    static int[] cover; // 每个位置被覆盖的次数
    static int[] blockCover; // 每个块的覆盖标记（延迟更新）
    
    // 离散化y坐标
    static void discretizeY() {
        Arrays.sort(yCoords, 0, m);
        int uniqueCount = 0;
        for (int i = 0; i < m; i++) {
            if (i == 0 || yCoords[i] != yCoords[i - 1]) {
                yCoords[uniqueCount++] = yCoords[i];
            }
        }
        m = uniqueCount;
    }
    
    // 初始化分块结构
    static void initBlocks() {
        blen = (int)Math.sqrt(m - 1);
        if (blen == 0) blen = 1;
        blockCount = (m - 1 + blen - 1) / blen;
        
        // 初始化覆盖数组
        cover = new int[m - 1];
        blockCover = new int[blockCount];
        Arrays.fill(cover, 0);
        Arrays.fill(blockCover, 0);
    }
    
    // 更新区间覆盖
    static void updateRange(int l, int r, int delta) {
        int leftBlock = l / blen;
        int rightBlock = r / blen;
        
        if (leftBlock == rightBlock) {
            // 所有元素都在同一个块内，直接暴力更新
            for (int i = l; i <= r; i++) {
                cover[i] += delta;
            }
        } else {
            // 处理左边不完整的块
            for (int i = l; i < (leftBlock + 1) * blen; i++) {
                cover[i] += delta;
            }
            
            // 处理中间完整的块（使用块标记）
            for (int i = leftBlock + 1; i < rightBlock; i++) {
                blockCover[i] += delta;
            }
            
            // 处理右边不完整的块
            for (int i = rightBlock * blen; i <= r; i++) {
                cover[i] += delta;
            }
        }
    }
    
    // 计算当前覆盖的总长度
    static double calculateCoveredLength() {
        double total = 0;
        
        for (int i = 0; i < m - 1; i++) {
            int blockIdx = i / blen;
            int totalCover = cover[i] + blockCover[blockIdx];
            
            if (totalCover > 0) {
                total += yCoords[i + 1] - yCoords[i];
            }
        }
        
        return total;
    }
    
    // 主函数，计算矩形覆盖的总面积
    static double solve() {
        // 离散化y坐标
        discretizeY();
        
        // 初始化分块结构
        initBlocks();
        
        // 按照x坐标排序扫描线
        Arrays.sort(lines, 0, 2 * n);
        
        double area = 0;
        for (int i = 0; i < 2 * n - 1; i++) {
            // 找到y1和y2在离散化数组中的位置
            int y1Pos = Arrays.binarySearch(yCoords, 0, m, lines[i].y1);
            int y2Pos = Arrays.binarySearch(yCoords, 0, m, lines[i].y2);
            
            // 更新覆盖区间
            updateRange(y1Pos, y2Pos - 1, lines[i].flag);
            
            // 计算当前扫描线到下一条扫描线之间的面积
            double currentLength = calculateCoveredLength();
            double deltaX = lines[i + 1].x - lines[i].x;
            area += currentLength * deltaX;
        }
        
        return area;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        int caseNum = 0;
        while (true) {
            String line = br.readLine();
            while (line != null && line.trim().isEmpty()) {
                line = br.readLine(); // 跳过空行
            }
            if (line == null) break;
            
            n = Integer.parseInt(line.trim());
            if (n == 0) break;
            
            caseNum++;
            m = 0;
            lines = new Line[2 * n];
            yCoords = new double[2 * n];
            
            // 读取每个矩形
            for (int i = 0; i < n; i++) {
                line = br.readLine();
                while (line != null && line.trim().isEmpty()) {
                    line = br.readLine(); // 跳过空行
                }
                
                StringTokenizer st = new StringTokenizer(line);
                double x1 = Double.parseDouble(st.nextToken());
                double y1 = Double.parseDouble(st.nextToken());
                double x2 = Double.parseDouble(st.nextToken());
                double y2 = Double.parseDouble(st.nextToken());
                
                // 添加两条扫描线
                lines[2 * i] = new Line(x1, y1, y2, 1);
                lines[2 * i + 1] = new Line(x2, y1, y2, -1);
                
                // 收集y坐标
                yCoords[m++] = y1;
                yCoords[m++] = y2;
            }
            
            double area = solve();
            pw.printf("Test case #%d\nTotal explored area: %.2f\n\n", caseNum, area);
        }
        
        pw.flush();
        pw.close();
        br.close();
    }
    
    /*
     * 时间复杂度分析：
     * - 离散化：O(n log n)
     * - 初始化分块：O(n)
     * - 扫描线排序：O(n log n)
     * - 每次更新操作：O(√n)
     * - 每次长度计算：O(n)
     * - 总体时间复杂度：O(n^2 + n√n)
     * 
     * 空间复杂度分析：
     * - 存储扫描线：O(n)
     * - 存储y坐标：O(n)
     * - 分块数据结构：O(n)
     * - 总体空间复杂度：O(n)
     * 
     * Java语言特性注意事项：
     * 1. 使用自定义Line类实现Comparable接口进行排序
     * 2. 使用Arrays.binarySearch进行二分查找
     * 3. 使用StringTokenizer处理输入
     * 4. 注意处理输入中的空行
     * 
     * 算法说明：
     * 扫描线算法是一种高效处理矩形覆盖问题的方法，通过从左到右扫描所有矩形的垂直边，
     * 维护当前的垂直覆盖状态，计算相邻扫描线之间的覆盖面积并累加。
     */
}