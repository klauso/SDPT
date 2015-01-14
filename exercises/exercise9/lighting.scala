package lighting

// Reminder: covariant refinement of abstract types:
//
//     trait A { type X <: Foo; trait Foo }
//     trait B extends A { type X <: Foo with Bar; trait Bar }
//     trait C extends A { type X <: Foo with Moo; trait Moo }
//     object D extends B with C { type X = Foo with Bar with Moo }


trait Base {

  type Location

  type Room <: Location

  type Composite <: Location with CompositeApi
  trait CompositeApi { self: Composite => 
    def locations: List[Location]
  }

  type Floor <: Composite with FloorApi
  trait FloorApi { self: Floor => 
    def rooms: List[Room]
    def locations = rooms
  }

  type House <: Composite with HouseApi
  trait HouseApi { self: House => 
    def floors: List[Floor]
    def locations = floors
  }
}

trait ShutterControl extends Base { 
  // Please implement the shutter component here
}

trait LightingControl extends Base {
  // Please implement the lighting component here
}

trait LightingUser { self: LightingControl =>
  val h1: House = ???
  h1.lights
}

trait ShutterUser { self: ShutterControl => 
  val h2: House = ???
  h2.shutters
}
