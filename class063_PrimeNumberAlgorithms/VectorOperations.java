// 向量运算的Java实现
// 包括：点积、叉积、线性相关性判定
// 时间复杂度：
// - 点积/叉积：O(n)
// - 线性相关性判定：O(n³)
// 空间复杂度：O(n²)

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VectorOperations {
    // 三维向量类
    public static class Vector3D {
        public double x, y, z;
        
        public Vector3D(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        public Vector3D() {
            this(0, 0, 0);
        }
        
        // 向量加法
        public Vector3D add(Vector3D v) {
            return new Vector3D(x + v.x, y + v.y, z + v.z);
        }
        
        // 向量减法
        public Vector3D subtract(Vector3D v) {
            return new Vector3D(x - v.x, y - v.y, z - v.z);
        }
        
        // 标量乘法
        public Vector3D multiply(double scalar) {
            return new Vector3D(x * scalar, y * scalar, z * scalar);
        }
        
        // 向量长度
        public double length() {
            return Math.sqrt(x*x + y*y + z*z);
        }
        
        // 单位向量
        public Vector3D normalize() {
            double len = length();
            if (Math.abs(len) < 1e-9) return new Vector3D(); // 避免除以零
            return new Vector3D(x/len, y/len, z/len);
        }
        
        // 打印向量
        @Override
        public String toString() {
            return "(" + x + ", " + y + ", " + z + ")";
        }
    }
    
    // 计算点积
    // 时间复杂度：O(1)
    public static double dotProduct(Vector3D a, Vector3D b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }
    
    // 计算叉积
    // 时间复杂度：O(1)
    public static Vector3D crossProduct(Vector3D a, Vector3D b) {
        return new Vector3D(
            a.y * b.z - a.z * b.y,
            a.z * b.x - a.x * b.z,
            a.x * b.y - a.y * b.x
        );
    }
    
    // 计算向量夹角（弧度）
    // 时间复杂度：O(1)
    public static double angleBetween(Vector3D a, Vector3D b) {
        double dot = dotProduct(a, b);
        double lenProduct = a.length() * b.length();
        if (Math.abs(lenProduct) < 1e-9) return 0;
        double cosTheta = dot / lenProduct;
        // 确保cosTheta在[-1, 1]范围内
        cosTheta = Math.max(Math.min(cosTheta, 1.0), -1.0);
        return Math.acos(cosTheta);
    }
    
    // 通用向量类（任意维度）
    public static class Vector {
        public List<Double> data;
        
        public Vector(int dim) {
            data = new ArrayList<>(dim);
            for (int i = 0; i < dim; i++) {
                data.add(0.0);
            }
        }
        
        public Vector(double[] values) {
            data = new ArrayList<>(values.length);
            for (double value : values) {
                data.add(value);
            }
        }
        
        // 获取维度
        public int dimension() {
            return data.size();
        }
        
        // 访问元素
        public double get(int i) {
            return data.get(i);
        }
        
        public void set(int i, double value) {
            data.set(i, value);
        }
        
        // 向量加法
        public Vector add(Vector v) {
            if (dimension() != v.dimension()) {
                throw new IllegalArgumentException("向量维度不匹配");
            }
            Vector result = new Vector(dimension());
            for (int i = 0; i < dimension(); i++) {
                result.set(i, get(i) + v.get(i));
            }
            return result;
        }
        
        // 向量减法
        public Vector subtract(Vector v) {
            if (dimension() != v.dimension()) {
                throw new IllegalArgumentException("向量维度不匹配");
            }
            Vector result = new Vector(dimension());
            for (int i = 0; i < dimension(); i++) {
                result.set(i, get(i) - v.get(i));
            }
            return result;
        }
        
        // 标量乘法
        public Vector multiply(double scalar) {
            Vector result = new Vector(dimension());
            for (int i = 0; i < dimension(); i++) {
                result.set(i, get(i) * scalar);
            }
            return result;
        }
        
        // 向量长度
        public double length() {
            double sum = 0;
            for (double x : data) {
                sum += x * x;
            }
            return Math.sqrt(sum);
        }
        
        // 打印向量
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < data.size(); i++) {
                sb.append(data.get(i));
                if (i < data.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(")");
            return sb.toString();
        }
    }
    
    // 计算两个任意维度向量的点积
    // 时间复杂度：O(n)
    public static double dotProduct(Vector a, Vector b) {
        if (a.dimension() != b.dimension()) {
            throw new IllegalArgumentException("向量维度不匹配");
        }
        double sum = 0;
        for (int i = 0; i < a.dimension(); i++) {
            sum += a.get(i) * b.get(i);
        }
        return sum;
    }
    
    // 高斯消元法判断向量组的线性相关性
    // 时间复杂度：O(n³)
    public static boolean isLinearlyDependent(List<Vector> vectors) {
        if (vectors.isEmpty()) return false;
        
        int m = vectors.size();      // 向量数量
        int n = vectors.get(0).dimension(); // 向量维度
        
        // 确保所有向量维度相同
        for (Vector v : vectors) {
            if (v.dimension() != n) {
                throw new IllegalArgumentException("向量维度不一致");
            }
        }
        
        // 构造增广矩阵进行高斯消元
        double[][] mat = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = vectors.get(i).get(j);
            }
        }
        
        int rank = 0;
        for (int col = 0; col < n && rank < m; col++) {
            // 寻找主元
            int pivot = rank;
            for (int i = rank; i < m; i++) {
                if (Math.abs(mat[i][col]) > Math.abs(mat[pivot][col])) {
                    pivot = i;
                }
            }
            
            // 如果主元为零，继续下一列
            if (Math.abs(mat[pivot][col]) < 1e-9) {
                continue;
            }
            
            // 交换行
            double[] temp = mat[rank];
            mat[rank] = mat[pivot];
            mat[pivot] = temp;
            
            // 归一化主行
            double div = mat[rank][col];
            for (int j = col; j < n; j++) {
                mat[rank][j] /= div;
            }
            
            // 消去其他行
            for (int i = 0; i < m; i++) {
                if (i != rank && Math.abs(mat[i][col]) > 1e-9) {
                    double factor = mat[i][col];
                    for (int j = col; j < n; j++) {
                        mat[i][j] -= factor * mat[rank][j];
                    }
                }
            }
            
            rank++;
        }
        
        // 如果秩小于向量数量，则线性相关
        return rank < m;
    }
    
    // 计算三个点是否共线
    public static boolean areCollinear(Vector3D a, Vector3D b, Vector3D c) {
        Vector3D ab = b.subtract(a);
        Vector3D ac = c.subtract(a);
        Vector3D cross = crossProduct(ab, ac);
        // 如果叉积的长度接近零，则三点共线
        return Math.abs(cross.length()) < 1e-9;
    }
    
    // 计算四个点是否共面
    public static boolean areCoplanar(Vector3D a, Vector3D b, Vector3D c, Vector3D d) {
        Vector3D ab = b.subtract(a);
        Vector3D ac = c.subtract(a);
        Vector3D ad = d.subtract(a);
        // 计算混合积，若为零则共面
        double tripleProduct = dotProduct(ab, crossProduct(ac, ad));
        return Math.abs(tripleProduct) < 1e-9;
    }
    
    // 力扣第1232题：缀点成线
    public static boolean checkStraightLine(int[][] coordinates) {
        if (coordinates.length <= 2) return true;
        
        // 取前两个点作为基准线
        int x0 = coordinates[0][0], y0 = coordinates[0][1];
        int x1 = coordinates[1][0], y1 = coordinates[1][1];
        
        // 使用向量叉积判断三点共线
        for (int i = 2; i < coordinates.length; i++) {
            int x2 = coordinates[i][0], y2 = coordinates[i][1];
            // 计算 (x1-x0)*(y2-y0) - (y1-y0)*(x2-x0)
            // 如果不为零，则三点不共线
            if ((x1 - x0) * (y2 - y0) - (y1 - y0) * (x2 - x0) != 0) {
                return false;
            }
        }
        
        return true;
    }
    
    // 主函数 - 测试代码
    public static void main(String[] args) {
        // 测试三维向量运算
        System.out.println("=== 三维向量运算测试 ===");
        Vector3D a = new Vector3D(1, 2, 3);
        Vector3D b = new Vector3D(4, 5, 6);
        
        System.out.println("向量a: " + a);
        System.out.println("向量b: " + b);
        
        System.out.println("点积 a·b = " + dotProduct(a, b));
        
        Vector3D cross = crossProduct(a, b);
        System.out.println("叉积 a×b = " + cross);
        
        double angle = angleBetween(a, b);
        System.out.println("夹角 θ = " + angle + " 弧度 = " + angle * 180 / Math.PI + " 度");
        
        // 测试共线性和共面性
        Vector3D c = new Vector3D(2, 4, 6); // c = 2a，应该与a和b共面
        System.out.println("\n点a, b, c共线？ " + (areCollinear(a, b, c) ? "是" : "否"));
        
        Vector3D d = new Vector3D(7, 8, 9);
        System.out.println("点a, b, c, d共面？ " + (areCoplanar(a, b, c, d) ? "是" : "否"));
        
        // 测试线性相关性
        System.out.println("\n=== 线性相关性测试 ===");
        
        // 线性相关的向量组
        List<Vector> dependentVectors = new ArrayList<>();
        dependentVectors.add(new Vector(new double[]{1, 2, 3}));
        dependentVectors.add(new Vector(new double[]{4, 5, 6}));
        dependentVectors.add(new Vector(new double[]{2, 3, 4})); // 这三个向量线性相关
        
        System.out.println("线性相关向量组:");
        for (Vector v : dependentVectors) {
            System.out.println(v);
        }
        System.out.println("线性相关？ " + (isLinearlyDependent(dependentVectors) ? "是" : "否"));
        
        // 线性无关的向量组
        List<Vector> independentVectors = new ArrayList<>();
        independentVectors.add(new Vector(new double[]{1, 0, 0}));
        independentVectors.add(new Vector(new double[]{0, 1, 0}));
        independentVectors.add(new Vector(new double[]{0, 0, 1})); // 这三个向量线性无关
        
        System.out.println("线性无关向量组:");
        for (Vector v : independentVectors) {
            System.out.println(v);
        }
        System.out.println("线性相关？ " + (isLinearlyDependent(independentVectors) ? "是" : "否"));
        
        // 测试力扣第1232题
        System.out.println("\n=== 力扣第1232题测试 ===");
        int[][] coords1 = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 7}};
        int[][] coords2 = {{1, 1}, {2, 2}, {3, 4}, {4, 5}, {5, 6}, {7, 7}};
        
        System.out.println("示例1: " + checkStraightLine(coords1)); // 应返回true
        System.out.println("示例2: " + checkStraightLine(coords2)); // 应返回false
        
        /*
         * 算法解释：
         * 1. 向量运算包括点积、叉积、线性相关性判定等基本操作
         * 2. 三维向量有专门的实现，支持常见的几何运算
         * 3. 通用向量类支持任意维度的向量运算
         * 4. 线性相关性判定使用高斯消元法计算向量组的秩
         * 
         * 时间复杂度分析：
         * - 点积/叉积：O(n)，其中n是向量维度
         * - 线性相关性判定：O(m²n)，其中m是向量数量，n是向量维度
         * 
         * 应用场景：
         * 1. 计算几何中的点、线、面关系判断
         * 2. 机器学习中的特征向量分析
         * 3. 物理学中的力、速度、加速度计算
         * 4. 计算机图形学中的变换和渲染
         * 
         * 相关题目：
         * 1. LeetCode 1232. Check If It Is a Straight Line - 检查是否为直线
         * 2. 向量点积、叉积相关问题 - 几何计算
         * 3. 向量线性相关性问题 - 线性代数
         */
    }
}