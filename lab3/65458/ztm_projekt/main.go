package main

import (
	"encoding/json"
	"log"
	"net/http"

	"github.com/gorilla/mux"
	"github.com/rs/cors"
)

// Result struct
type Result struct {
	Transports []Transport `json:"result"`
}

// Transport Struct (Model)
type Transport struct {
	Lines         string  `json:"Lines"`
	Lon           float32 `json:"Lon"`
	Lat           float32 `json:"Lat"`
	VehicleNumber string  `json:"VehicleNumber"`
	Time          string  `json:"Time"`
	Brigade       string  `jsno:"Brigade"`
}

func enableCors(w *http.ResponseWriter) {
	(*w).Header().Set("Access-Control-Allow-Origin", "*")
	(*w).Header().Set("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE")
	(*w).Header().Set("Access-Control-Allow-Headers", "Accept, Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization")

}

func getTransport(w http.ResponseWriter, r *http.Request) {
	//enableCors(&w)

	w.Header().Set("Content-Type", "application/json")

	params := mux.Vars(r) // Get params
	resp, err := http.Get("https://api.um.warszawa.pl/api/action/busestrams_get/?resource_id=f2e5503e927d-4ad3-9500-4ab9e55deb59&apikey=76674579-7201-42c7-a7ac-2e73554a18de&type=1&line=" + params["id"])
	if err != nil {
		log.Fatalln(err)
	}

	resp.Body = http.MaxBytesReader(w, resp.Body, 1048576)

	dec := json.NewDecoder(resp.Body)
	dec.DisallowUnknownFields()

	var t Result
	dec.Decode(&t)

	json.NewEncoder(w).Encode(t.Transports)
}

func main() {
	// Init router
	r := mux.NewRouter()

	// Route Handlers / Endpoints
	r.HandleFunc("/api/transport/{id}", getTransport).Methods("GET")
	handler := cors.Default().Handler(r)
	http.ListenAndServe(":8000", handler)
}
