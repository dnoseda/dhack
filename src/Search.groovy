import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


class Search {

	static main(args) {
		
		
		def initial = new GregorianCalendar(2012, Calendar.JUNE, 10, 23, 59)
		def results = [:]
		def range = 2
		def daysBetween = 15
		def show = { k,it ->
			println "$k\t${it.stops}\t${it.price}\t${it.from}\t${it.to}\t${it.dest}"
		}

		for(int i=0; i < 10; i++){
			for(String dest: [ "BCN", "ROM", "LON","LIS", "MAD"]){
				def from = initial.clone()
				from.add(Calendar.DATE, i)
				def to = from.clone()
				to.add(Calendar.DATE, daysBetween)
				def froms = String.format('%tF', from)
				def tos = String.format('%tF', to)
				def url = "http://www.despegar.com.ar/Flights.Services/Flights/Flights.svc/ClusteredFlights/BUE/${dest}/${froms}/${tos}/1/0/0"
				def fUrl = "http://www.despegar.com.ar/search/flights/RoundTrip/BUE/${dest}/${froms}/${tos}/1/0/0"
				if(!results[fUrl]){
					println "a $dest fecha: $froms volviendo el $tos..."
					try{
						def searchResult = JSONObject.fromObject (url.toURL().getText())
						results[fUrl] = searchResult.Boxs.collect({[stops:it.Dep[0].Segmts?.Stops?.size(), 
						from:froms, to:tos, price:it.Itns[0].Tot?.Loc, dest:dest]})
						println "wot: ${results[fUrl]}"
						results[fUrl].each{v ->
							print "parcial: "
							println "$fUrl\t${v.stops}\t${v.price}\t${v.from}\t${v.to}\t${v.dest}"
						}
					}catch(Exception e){
						println e
					}
				}
			}
		}
		results.each{k, v ->
			v.each{
				show(k,v)
			}
		}

	}
}
