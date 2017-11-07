/*jshint browser:true*/
/*global MashupPlatform*/

(function() {

	"use strict";
	var SalaDeAula = function SalaDeAula() {

	};

	SalaDeAula.prototype.init = function init() {
		
	};
	
	var process_input = function process_input(input) {
        var info;

        info = JSON.parse(input);

		if(MashupPlatform.prefs.get('id_sala') != info.id){
			return;
		}

		$("#tempAtual").text(info.temperature);

			console.log(info.presence+" / "+info.brightness+" / "+info.temperature+" / "+info.time);
			if(info.presence == 1){
				$('#teste').css({backgroundImage : 'url(images/sala_luz_ligada.png)'});
				$("#computador").attr("src","images/computador_ligado.png");
				$("#projetor").attr("src","images/projetor_ligado.png");
				$("#ar").attr("src","images/ar_ligado.png");

				var options = {
					useEasing : true,
					useGrouping : true,
					separator : ',',
					decimal : '.',
					prefix : '',
					suffix : ''
				};

				var demo;
				
				if(info.time == 0){
					$("#professor").attr("src","images/professor_1.png");
					if(info.temperature != 22){
						demo = new CountUp("tempAtual", info.temperature, 22, 0, 10, options);
						demo.start();
					}
				}else if(info.time == 1){
					$("#professor").attr("src","images/professor_2.png");
					if(info.temperature != 17){
						demo = new CountUp("tempAtual", info.temperature, 17, 0, 10, options);
						demo.start();
					}
				}else if(info.time == 2){
					$("#professor").attr("src","images/professor_3.png");
					if(info.temperature != 20){
						demo = new CountUp("tempAtual", info.temperature, 20, 0, 10, options);
						demo.start();
					}
				}else if(info.time == 3){
					$("#professor").attr("src","images/professor_4.png");
					if(info.temperature != 24){
						demo = new CountUp("tempAtual", info.temperature, 24, 0, 10, options);
						demo.start();
					}
				}else{
					$("#professor").attr("src","images/professor_1.png");
					if(info.temperature != 21){
						demo = new CountUp("tempAtual", info.temperature, 21, 0, 10, options);
						demo.start();
					}
				}
				$("#professor").show();
			}else{
				$('#teste').css({backgroundImage : 'url(images/sala_luz_desligada.png)'});
				$("#computador").attr("src","images/computador_desligado.png");
				$("#projetor").attr("src","images/projetor_desligado.png");
				$("#ar").attr("src","images/ar_desligado.png");
				$("#professor").hide();
			}

			if(info.brightness > 50){
				$('#teste').css({backgroundImage : 'url(images/sala_luz_desligada.png)'});
			}


    };

	window.SalaDeAula = SalaDeAula;
	
	MashupPlatform.wiring.registerCallback('input', process_input);

})();

var salaDeAula = new SalaDeAula();

window.addEventListener("DOMContentLoaded", salaDeAula.init.bind(salaDeAula), false);
