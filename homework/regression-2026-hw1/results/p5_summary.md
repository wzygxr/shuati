# Problem 5 结果摘要

**四个（及补充）模型的比较（n=364）**

| model | k | RSS | R2 | R2adj | AIC | BIC | LOOCV |
| --- | --- | --- | --- | --- | --- | --- | --- |
| natural cubic spline K=10 | 10.0000 | 9.8676e+08 | 0.6185 | 0.6088 | 5411.8528 | 5450.8243 | 2.8513e+06 |
| cubic spline K=7 | 11.0000 | 9.8613e+08 | 0.6187 | 0.6079 | 5413.6220 | 5456.4907 | 2.8573e+06 |
| smoothing spline (edf=8.3) | 8.3500 | 9.9645e+08 | 0.6148 | 0.6068 | 5412.1020 | 5444.6265 | 2.8654e+06 |
| natural spline K=4 (BIC choice) | 4.0000 | 1.0290e+09 | 0.6022 | 0.5989 | 5415.1146 | 5430.7032 | 2.8851e+06 |
| cubic spline K=2 (BIC choice) | 6.0000 | 1.0190e+09 | 0.6060 | 0.6005 | 5415.5489 | 5438.9319 | 2.9081e+06 |
| cubic polynomial | 4.0000 | 1.0528e+09 | 0.5930 | 0.5896 | 5423.4456 | 5439.0342 | 2.9565e+06 |


## 结点个数 / 光滑参数的选取

**各准则选出的结点个数（平滑样条为 GCV 选出的有效自由度）**

| kind | BIC | AIC | LOOCV |
| --- | --- | --- | --- |
| cubic | 2 | 7 | 7 |
| natural | 4 | 10 | 10 |
| smoothing | GCV-λ | GCV-λ | 8.3500 |


## 拟合曲线最低点（用电量最小对应的温度，°C）

| model | T at minimum |
| --- | --- |
| cubic polynomial | 13.1523 |
| cubic spline K=7 | 8.7350 |
| natural cubic spline K=10 | 8.6279 |
| smoothing spline (edf=8.3) | 8.6279 |


## 平滑样条

GCV 最优 λ = 21.135（edf = 8.35），自定义实现 RSS = 996450723，scipy.make_smoothing_spline RSS = 996732597（两者一致，交叉验证通过）。


## 残差 Durbin–Watson 统计量（若明显小于 2，说明残差存在自相关）

| model | DW |
| --- | --- |
| cubic polynomial | 0.6826 |
| cubic spline K=7 | 0.7668 |
| natural cubic spline K=10 | 0.7654 |
| smoothing spline (edf=8.3) | 0.7248 |
