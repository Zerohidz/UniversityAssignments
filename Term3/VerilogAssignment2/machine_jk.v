module machine_jk(
    input wire x,
    input wire CLK,
    input wire RESET,
    output wire F,
    output wire [2:0] S
);

    // Excitation Table
    // Q Q+ J  K
    // 0 0  0  X
    // 0 1  1  X
    // 1 0  X  1
    // 1 1  X  0

    // A A+   Ja Ka      B B+    Jb Kb       C C+   Jc Kc
    // 0 0    0  X       0 1     1  X        0 0    0  0 
    // 0 0    0  X       0 0     0  X        0 1    1  X 
    // 0 0    0  X       0 1     1  X        1 1    X  0 
    // 0 0    0  X       0 0     0  X        1 0    X  1 
    // 0 1    1  X       1 0     X  1        0 0    0  0 
    // 0 0    0  X       1 1     X  0        0 1    1  X 
    // 0 1    1  X       1 0     X  1        1 1    X  0 
    // 0 0    0  X       1 1     X  0        1 0    X  1 
    // 1 1    X  0       0 1     1  X        0 0    0  0 
    // 1 1    X  0       0 0     0  X        0 1    1  X 
    // 1 1    X  0       0 1     1  X        1 1    X  0 
    // 1 1    X  0       0 0     0  X        1 0    X  1 
    // 1 1    X  0       1 1     X  0        0 0    0  0 
    // 1 1    X  0       1 1     X  0        0 1    1  X 
    // 1 1    X  0       1 1     X  0        1 1    X  0 
    // 1 1    X  0       1 1     X  0        1 0    X  1 

    // A B C x  A+ B+ C+  Ja Ka   Jb Kb   Jc Kc   y
    // 0 0 0 0  0  1  0   0  X    1  X    0  0    0
    // 0 0 0 1  0  0  1   0  X    0  X    1  X    0
    // 0 0 1 0  0  1  1   0  X    1  X    X  0    0
    // 0 0 1 1  0  0  0   0  X    0  X    X  1    0
    // 0 1 0 0  1  0  0   1  X    X  1    0  0    0
    // 0 1 0 1  0  1  1   0  X    X  0    1  X    0
    // 0 1 1 0  1  0  1   1  X    X  1    X  0    0
    // 0 1 1 1  0  1  0   0  X    X  0    X  1    0
    // 1 0 0 0  1  1  0   X  0    1  X    0  0    0
    // 1 0 0 1  1  0  1   X  0    0  X    1  X    0
    // 1 0 1 0  1  1  1   X  0    1  X    X  0    0
    // 1 0 1 1  1  0  0   X  0    0  X    X  1    0
    // 1 1 0 0  1  1  0   X  0    X  0    0  0    1
    // 1 1 0 1  1  1  1   X  0    X  0    1  X    1
    // 1 1 1 0  1  1  1   X  0    X  0    X  0    0
    // 1 1 1 1  1  1  0   X  0    X  0    X  1    0

    // Ja = Bx'
    // Ka = 0 / A'
    // Jb = x'
    // Kb = A'x'
    // Jc = x
    // Kc = x
    // y  = ABC'
    
    wire x_not, A_not, C_not;
    not(x_not, x);
    not(A_not, S[2]);
    not(C_not, S[0]);

    wire Ja;
    and(Ja, x_not, S[1]);

    wire Jb, Kb;
    not(Jb, x);
    and(Kb, A_not, x_not);

    jkff A_ff(.J(Ja), .K(0), .CLK(CLK), .RESET(RESET), .Q(S[2]));
    jkff B_ff(.J(Jb), .K(Kb), .CLK(CLK), .RESET(RESET), .Q(S[1]));
    jkff C_ff(.J(x), .K(x), .CLK(CLK), .RESET(RESET), .Q(S[0]));

    and(F, S[2], S[1], C_not);

    endmodule