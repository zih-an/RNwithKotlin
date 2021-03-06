package com.rnwithkotlin.utils
import Jama.Matrix
import java.lang.Double.isNaN
import org.jblas.*
import org.jblas.Solve
import kotlin.math.sqrt

class AffineTransprocess {
    //姿势棒图关节点数目
    val num_points:Int=17
    //姿势棒图骨向量数目
    val num_vector:Int=18
    //关节点的维度数目
    val num_dimension:Int=2
//__________________________________jblas__________________________________
    //处理骨架的NaN数据，以便于进行最小二乘法时候去除对应的方程
    fun NanMesg(X:DoubleMatrix,Y:DoubleMatrix):DoubleMatrix
    {
        val num:Int=X.rows
        val NanMesg:DoubleMatrix= DoubleMatrix(num,1)
        for(i in 0..num-1)
        {
            if(isNaN(X[i,0])||isNaN(X[i,1])||isNaN(Y[i,0])||isNaN(Y[i,1]))
            {
                NanMesg.put(i,0,0.0)
                //将相应数据置为0
                X.put(i,0,0.0)
                X.put(i,1,0.0)
                Y.put(i,0,0.0)
                Y.put(i,1,0.0)
            }
            else
            {
                NanMesg.put(i,0,1.0)
            }
        }
        return NanMesg
    }

    //仿射变换，使用最小二乘法解超定方程组AX+b≈Y，并输出Y_hat
    fun affine_transfor(X:DoubleMatrix,Y:DoubleMatrix):DoubleMatrix
    {
        //如果X或者Y中有NaN值，则去掉相应的方程
        val len=X.rows
        val m:DoubleMatrix=NanMesg(X,Y)
//        val para=
//        equations {
//                //A=|a11 a12|    b=|b1|
//                //  |a21 a22|      |b2|
//                //a11   a12    a21    a22    b1             b2
//            this[X[0,0],X[0,1],0     ,0     ,m[0,0]*1      ,m[0,0]*0      ]= Y[0,0]
//            this[0     ,0     ,X[0,0],X[0,1],m[0,0]*0      ,m[0,0]*1      ]= Y[0,1]
//
//            this[X[1,0],X[1,1],0     ,0     ,m[1,0]*1      ,m[1,0]*0      ]= Y[1,0]
//            this[0     ,0     ,X[1,0],X[1,1],m[1,0]*0      ,m[1,0]*1      ]= Y[1,1]
//
//            this[X[2,0],X[2,1],0     ,0     ,m[2,0]*1      ,m[2,0]*0      ]= Y[2,0]
//            this[0     ,0     ,X[2,0],X[2,1],m[2,0]*0      ,m[2,0]*1      ]= Y[2,1]
//
//            this[X[3,0],X[3,1],0     ,0     ,m[3,0]*1      ,m[3,0]*0      ]= Y[3,0]
//            this[0     ,0     ,X[3,0],X[3,1],m[3,0]*0      ,m[3,0]*1      ]= Y[3,1]
//
//            this[X[4,0],X[4,1],0     ,0     ,m[4,0]*1      ,m[4,0]*0      ]= Y[4,0]
//            this[0     ,0     ,X[4,0],X[4,1],m[4,0]*0      ,m[4,0]*1      ]= Y[4,1]
//
//            this[X[5,0],X[5,1],0     ,0     ,m[5,0]*1      ,m[5,0]*0      ]= Y[5,0]
//            this[0     ,0     ,X[5,0],X[5,1],m[5,0]*0      ,m[5,0]*1      ]= Y[5,1]
//
//            this[X[6,0],X[6,1],0     ,0     ,m[6,0]*1      ,m[6,0]*0      ]= Y[6,0]
//            this[0     ,0     ,X[6,0],X[6,1],m[6,0]*0      ,m[6,0]*1      ]= Y[6,1]
//
//            this[X[7,0],X[7,1],0     ,0     ,m[7,0]*1      ,m[7,0]*0      ]= Y[7,0]
//            this[0     ,0     ,X[7,0],X[7,1],m[7,0]*0      ,m[7,0]*1      ]= Y[7,1]
//
//            this[X[8,0],X[8,1],0     ,0     ,m[8,0]*1      ,m[8,0]*0      ]= Y[8,0]
//            this[0     ,0     ,X[8,0],X[8,1],m[8,0]*0      ,m[8,0]*1      ]= Y[8,1]
//
//            this[X[9,0],X[9,1],0     ,0     ,m[9,0]*1      ,m[9,0]*0      ]= Y[9,0]
//            this[0     ,0     ,X[9,0],X[9,1],m[9,0]*0      ,m[9,0]*1      ]= Y[9,1]
//
//            this[X[10,0],X[10,1],0     ,0     ,m[10,0]*1      ,m[10,0]*0      ]= Y[10,0]
//            this[0     ,0     ,X[10,0],X[10,1],m[10,0]*0      ,m[10,0]*1      ]= Y[10,1]
//
//            this[X[11,0],X[11,1],0     ,0     ,m[11,0]*1      ,m[11,0]*0      ]= Y[11,0]
//            this[0     ,0     ,X[11,0],X[11,1],m[11,0]*0      ,m[11,0]*1      ]= Y[11,1]
//
//            this[X[12,0],X[12,1],0     ,0     ,m[12,0]*1      ,m[12,0]*0      ]= Y[12,0]
//            this[0     ,0     ,X[12,0],X[12,1],m[12,0]*0      ,m[12,0]*1      ]= Y[12,1]
//
//            this[X[13,0],X[13,1],0     ,0     ,m[13,0]*1      ,m[13,0]*0      ]= Y[13,0]
//            this[0     ,0     ,X[13,0],X[13,1],m[13,0]*0      ,m[13,0]*1      ]= Y[13,1]
//
//            this[X[14,0],X[14,1],0     ,0     ,m[14,0]*1      ,m[14,0]*0      ]= Y[14,0]
//            this[0     ,0     ,X[14,0],X[14,1],m[14,0]*0      ,m[14,0]*1      ]= Y[14,1]
//
//            this[X[15,0],X[15,1],0     ,0     ,m[15,0]*1      ,m[15,0]*0      ]= Y[15,0]
//            this[0     ,0     ,X[15,0],X[15,1],m[15,0]*0      ,m[15,0]*1      ]= Y[15,1]
//
//            this[X[16,0],X[16,1],0     ,0     ,m[16,0]*1      ,m[16,0]*0      ]= Y[16,0]
//            this[0     ,0     ,X[16,0],X[16,1],m[16,0]*0      ,m[16,0]*1      ]= Y[16,1]
//
//            this[X[17,0],X[17,1],0     ,0     ,m[17,0]*1      ,m[17,0]*0      ]= Y[17,0]
//            this[0     ,0     ,X[17,0],X[17,1],m[17,0]*0      ,m[17,0]*1      ]= Y[17,1]
//
//        }
//            .also { println(it.matrixView()) }
//            .solve()



        //将超定方程化为适定方程，进行最小二乘法计算求解
        //        |a00|
        //        |a01|
        //        |a10|
        //(T')T * |a11|  = (T')H   求解适定方程组
        //        |b1 |
        //        |b2 |
        //初始化矩阵T和H
        var T:DoubleMatrix=DoubleMatrix(2*len,6)
        var H:DoubleMatrix=DoubleMatrix(2*len,1)
        for(i in 0..len-1)
        {
            T.put(i,0,X.get(i,0))
            T.put(i,1,X.get(i,1))
            T.put(i,4,1*m[i,0])

            T.put(i+1,2,X.get(i,0))
            T.put(i+1,3,X.get(i,1))
            T.put(i+1,5,1*m[i,0])

            H.put(i,0,Y.get(i,0))
            H.put(i+1,0,Y.get(i,1))
        }

        //调用solve求解最小二乘法方程组




        val para=Solve.solve(T.transpose().mmul(T),T.transpose().mmul(H))
        println(para)

        //从param取出A和b
        var A:DoubleMatrix= DoubleMatrix(num_dimension,num_dimension)
        var b:DoubleMatrix= DoubleMatrix(num_dimension,1)
        if(para?.get(0)!=null&&para?.get(1)!=null&&para?.get(2)!=null&&para?.get(3)!=null)
        {
            A.put(0,0,para?.get(0) )
            A.put(0,1,para?.get(1) )
            A.put(1,0,para?.get(2) )
            A.put(1,1,para?.get(3) )
        }
        if(para?.get(4)!=null&&para?.get(5)!=null)
        {
            b.put(0,0,para?.get(4))
            b.put(1,0,para?.get(5))
        }

        //计算Y_hat=Ax+b
        var Y_hat:DoubleMatrix
        println(A)
        println(b)
        Y_hat= A.mmul(X.transpose()).transpose()
        for(i in 0..len-1)
        {
            if(m[i,0]==1.0.toDouble())
            {
                Y_hat.put(i,0,Y_hat.get(i,0)+ b[0, 0])
                Y_hat.put(i,1,Y_hat.get(i,1)+ b[1, 0])
            }
            else
            {
                Y_hat.put(i,0,Double.NaN)
                Y_hat.put(i,1,Double.NaN)
            }
        }
//        println("error:")
//        println(Y_hat.sub(Y).norm2())
        return Y_hat
    }

    fun affine_transfor_DMlist(Xs:List<DoubleMatrix>,Ys:List<DoubleMatrix>): List<DoubleMatrix>
    {
        var affter_affineTransfor_MatrixList:MutableList<DoubleMatrix> = arrayListOf<DoubleMatrix>()
        val sampleNum=Xs.count()

        for(i in 0..sampleNum-1 )
        {
            val temp=affine_transfor(Xs.get(i),Ys.get(i))
            affter_affineTransfor_MatrixList.add(temp)
        }
        return affter_affineTransfor_MatrixList
    }

//__________________________________jama__________________________________

    fun NanMesg_Jama(X:Matrix,Y:Matrix):Matrix
    {
        val num:Int=X.rowDimension
        val NanMesg:Matrix= Matrix(num,1)
        for(i in 0..num-1)
        {
            if(isNaN(X[i,0])||isNaN(X[i,1])||isNaN(Y[i,0])||isNaN(Y[i,1]))
            {
                NanMesg.set(i,0,0.0)
                //将相应数据置为0
                X.set(i,0,0.0)
                X.set(i,1,0.0)
                Y.set(i,0,0.0)
                Y.set(i,1,0.0)
            }
            else
            {
                NanMesg.set(i,0,1.0)
            }
        }
        return NanMesg
    }

    //仿射变换，使用最小二乘法解超定方程组AX+b≈Y，并输出Y_hat
    fun affine_transfor_Jama(X:Matrix,Y:Matrix):Matrix
    {
        //如果X或者Y中有NaN值，则去掉相应的方程
        val len=X.rowDimension
        val m:Matrix = NanMesg_Jama(X,Y)

        var T:Matrix=Matrix(2*len,6)
        var H:Matrix=Matrix(2*len,1)

        for(i in 0..len-1)
        {
            T.set(i,0,X.get(i,0))
            T.set(i,1,X.get(i,1))
            T.set(i,4,1*m[i,0])

            T.set(i+1,2,X.get(i,0))
            T.set(i+1,3,X.get(i,1))
            T.set(i+1,5,1*m[i,0])

            H.set(i,0,Y.get(i,0))
            H.set(i+1,0,Y.get(i,1))
        }

        //调用solve求解最小二乘法方程组
        val para=T.transpose().times(T).solve(T.transpose().times(H))
        printJama(para)

        //从param取出A和b
        var A:Matrix= Matrix(num_dimension,num_dimension)
        var b:Matrix= Matrix(num_dimension,1)



        A.set(0,0,para.get(0,0))
        A.set(0,1,para.get(1,0))
        A.set(1,0,para.get(2,0))
        A.set(1,1,para.get(3,0))
        b.set(0,0,para.get(4,0))
        b.set(1,0,para.get(5,0))

        //计算Y_hat=Ax+b
        var Y_hat:Matrix
        Y_hat= A.times(X.transpose()).transpose()
        for(i in 0..len-1)
        {
            if(m[i,0]==1.0.toDouble())
            {
                Y_hat.set(i,0,Y_hat.get(i,0)+ b[0, 0])
                Y_hat.set(i,1,Y_hat.get(i,1)+ b[1, 0])
            }
            else
            {
                Y_hat.set(i,0,Double.NaN)
                Y_hat.set(i,1,Double.NaN)
            }
        }
//        println("error:")
//        println(Y_hat.sub(Y).norm2())
        return Y_hat
    }

    fun affine_transfor_DMlist_Java(Xs:List<Matrix>,Ys:List<Matrix>): List<Matrix>
    {
        var affter_affineTransfor_MatrixList:MutableList<Matrix> = arrayListOf<Matrix>()
        val sampleNum=Xs.count()
        for(i in 0..sampleNum-1 )
        {
            val temp=affine_transfor_Jama(Xs.get(i),Ys.get(i))
            affter_affineTransfor_MatrixList.add(temp)
        }
        return affter_affineTransfor_MatrixList
    }

    fun printJama(obj:Matrix)
    {
        println("------------------")
        for(i in 0..obj.rowDimension-1)
        {
            for(j in 0..obj.columnDimension-1)
            {
                println(obj[i,j])
            }
        }
        println("------------------")
    }


}
fun main(args: Array<String>) {




//    var xx:Matrix= Matrix(18,2)
//    var yy:Matrix= Matrix(18,2)
//    for(i in 0..17)
//    {
//        xx.set(i,0,(i).toDouble())
//        xx.set(i,1,(i*i).toDouble())
//        yy.set(i,0,(21*xx[i,0]+2*xx[i,1]+7).toDouble())
//        yy.set(i,1,(7*xx[i,0]+11*xx[i,1]+15).toDouble())
//
//    }
//    println(AffineTransprocess().affine_transfor_Jama(xx,yy))



    var xx:Matrix= Matrix(4,2)
    var yy:Matrix= Matrix(4,2)
    for(i in 0..3)
    {
        xx.set(i,0,(i).toDouble())
        xx.set(i,1,(2*i+sqrt(i.toDouble())).toDouble())
        yy.set(i,0,(xx[i,0]+xx[i,1]).toDouble())
        yy.set(i,1,(2*xx[i,0]+3*xx[i,1]).toDouble())

    }

    AffineTransprocess().affine_transfor_Jama(xx,yy)

}