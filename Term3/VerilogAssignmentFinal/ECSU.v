`timescale 1us / 1ps

module ECSU(
        input CLK,
        input RST,
        input thunderstorm,
        input [5:0] wind,
        input [1:0] visibility,
        input signed [7:0] temperature,
        output reg severe_weather,
        output reg emergency_landing_alert,
        output reg [1:0] ECSU_state
    );

    initial begin 
        severe_weather = 0;
        emergency_landing_alert = 0;
        ECSU_state = 2'b00;
    end
    
    always @(posedge CLK) begin
        if (RST) begin
            severe_weather <= 0;
            emergency_landing_alert <= 0;
            ECSU_state <= 2'b00;
        end
        else begin
            case (ECSU_state)
                2'b00: begin
                    if ((wind <= 15 & wind > 10) | visibility == 2'b01) begin
                        ECSU_state <= 2'b01;
                    end
                    else if (thunderstorm | wind > 15 | temperature > 35 | temperature < -35 | visibility == 2'b11) begin
                        ECSU_state <= 2'b10;
                    end
                end
                2'b01: begin
                    if (wind <= 10 & visibility == 2'b00) begin
                        ECSU_state <= 2'b00;
                    end
                    else if (thunderstorm | wind > 15 | temperature > 35 | temperature < -35 | visibility == 2'b11) begin
                        ECSU_state <= 2'b10;
                    end
                end
                2'b10: begin
                    if (temperature > 40 | temperature < -40 | wind > 20) begin
                        ECSU_state <= 2'b11;
                    end
                    else if (thunderstorm == 0 & wind <= 10 & temperature >= -35 & temperature <= 35 & visibility == 2'b01) begin
                        ECSU_state <= 2'b01;
                    end
                end
                default:;
            endcase
        end
    end

    always @* begin
        case (ECSU_state)
            2'b00, 2'b01: begin
                if (thunderstorm | wind > 15 | temperature > 35 | temperature < -35 | visibility == 2'b11) begin
                    severe_weather <= 1;
                end
            end
            2'b10: begin
                if (temperature > 40 | temperature < -40 | wind > 20) begin
                    emergency_landing_alert <= 1;
                end
                else if (thunderstorm == 0 & wind <= 10 & temperature >= -35 & temperature <= 35 & visibility == 2'b01) begin
                    severe_weather <= 0;
                end
            end
            default:;
        endcase
    end
endmodule
