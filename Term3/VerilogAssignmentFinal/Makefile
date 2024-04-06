CC = iverilog 
FLAGS = -Wall 

artau:
	$(CC) $(FLAGS) -o artau_t ARTAU.v ARTAU_tb.v
	vvp artau_t
	gtkwave ARTAU.wave ARTAU.gtkw

ecsu:
	$(CC) $(FLAGS) -o ecsu_t ECSU.v ECSU_tb.v
	vvp ecsu_t
	gtkwave ECSU.wave ECSU.gtkw

icms:
	$(CC) $(FLAGS) -o icms_t ICMS.v ICMS_tb.v ARTAU.v ECSU.v
	vvp icms_t
	gtkwave ICMS.wave ICMS.gtkw

clean:
	rm *.wave
	rm artau_t
	rm ecsu_t
	rm icms_t