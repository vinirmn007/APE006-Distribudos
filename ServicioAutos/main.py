from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from contextlib import asynccontextmanager
import time

from database import engine, Base, SessionLocal
from models import Auto
import schemas
from sensor_manager import start_sensor_simulation

@asynccontextmanager
async def lifespan(app: FastAPI):
    # Intentar crear tablas (esperando a que MariaDB esté listo)
    retries = 5
    while retries > 0:
        try:
            Base.metadata.create_all(bind=engine)
            print("Tablas creadas en MariaDB exitosamente.")
            break
        except Exception as e:
            print(f"Esperando a MariaDB... reintentos restantes: {retries}")
            retries -= 1
            time.sleep(5)
    
    # Iniciar simulación de sensores en segundo plano
    start_sensor_simulation()
    yield

app = FastAPI(lifespan=lifespan, title="Servicio de Autos API")

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@app.post("/autos/", response_model=schemas.AutoResponse)
def create_auto(auto: schemas.AutoCreate, db: Session = Depends(get_db)):
    db_auto = db.query(Auto).filter(Auto.placa == auto.placa).first()
    if db_auto:
        raise HTTPException(status_code=400, detail="Placa ya registrada")
    
    nuevo_auto = Auto(
        placa=auto.placa,
        modelo=auto.modelo,
        color=auto.color,
        anio=auto.anio
    )
    db.add(nuevo_auto)
    db.commit()
    db.refresh(nuevo_auto)
    return nuevo_auto

@app.get("/autos/", response_model=list[schemas.AutoResponse])
def get_autos(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    autos = db.query(Auto).offset(skip).limit(limit).all()
    return autos
