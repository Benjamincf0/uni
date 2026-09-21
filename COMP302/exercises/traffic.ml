let must_stop (distance : int) (light : traffic_light) : bool = 
  match (light, distance) with
  | (Red, _) -> true
  | (Yellow, d) when d > 100 -> true
  | (_, _) -> false